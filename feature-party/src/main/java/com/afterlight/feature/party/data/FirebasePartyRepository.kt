package com.afterlight.feature.party.data

import android.content.Context
import android.util.Log
import com.afterlight.core.network.ConnectivityObserver
import com.afterlight.data.local.dao.PartyDao
import com.afterlight.data.local.model.PartyEntity
import com.afterlight.data.remote.firebase.FirebasePartyService
import com.afterlight.feature.party.domain.PartyRepository
import com.afterlight.feature.party.worker.PartyExpirationScheduler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase-backed implementation of PartyRepository.
 * Handles online real-time syncing of active parties and syncing offline writes to local Room cache.
 */
@Singleton
class FirebasePartyRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val partyService: FirebasePartyService,
    private val partyDao: PartyDao,
    private val expirationScheduler: PartyExpirationScheduler,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val connectivityObserver: ConnectivityObserver
) : PartyRepository {

    private companion object {
        const val TAG = "FirebasePartyRepo"
    }

    private var partyListener: ListenerRegistration? = null
    private var isSyncing = false
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        // Automatically start/stop syncing when auth state changes
        auth.addAuthStateListener { firebaseAuth ->
            val userId = firebaseAuth.currentUser?.uid
            if (userId != null) {
                startSyncing(userId)
            } else {
                stopSyncing()
            }
        }

        // Handle offline -> online transitions using ConnectivityObserver
        repositoryScope.launch {
            connectivityObserver.isConnected.collectLatest { connected ->
                try {
                    if (connected) {
                        Log.d(TAG, "Device online. Enabling Firestore network.")
                        firestore.enableNetwork().await()
                    } else {
                        Log.d(TAG, "Device offline. Disabling Firestore network.")
                        firestore.disableNetwork().await()
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Error adjusting Firestore network state: ${e.message}")
                }
            }
        }
    }

    private fun startSyncing(userId: String) {
        if (isSyncing) return
        isSyncing = true
        Log.d(TAG, "Starting real-time Firestore sync for user: $userId")

        partyListener = firestore.collection("parties")
            .whereArrayContains("members", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error in snapshot listener: ${error.message}", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    repositoryScope.launch {
                        try {
                            val activeParties = mutableListOf<PartyEntity>()
                            val now = Clock.System.now()

                            for (doc in snapshot.documents) {
                                val id = doc.id
                                val name = doc.getString("name") ?: ""
                                val hostUserId = doc.getString("hostUserId") ?: ""
                                val createdAtTimestamp = doc.getTimestamp("createdAt")
                                val expiresAtTimestamp = doc.getTimestamp("expiresAt")
                                val isActive = doc.getBoolean("isActive") ?: true

                                val createdAt = createdAtTimestamp?.let { 
                                    Instant.fromEpochMilliseconds(it.toDate().time) 
                                } ?: now
                                
                                val expiresAt = expiresAtTimestamp?.let { 
                                    Instant.fromEpochMilliseconds(it.toDate().time) 
                                } ?: now

                                if (isActive && expiresAt > now) {
                                    val party = PartyEntity(
                                        id = id,
                                        name = name,
                                        hostUserId = hostUserId,
                                        createdAt = createdAt,
                                        expiresAt = expiresAt,
                                        isDeleted = false
                                    )
                                    activeParties.add(party)
                                    
                                    // Make sure expiration is scheduled locally
                                    expirationScheduler.scheduleExpiration(id, expiresAt)
                                } else {
                                    // Mark inactive/expired locally
                                    partyDao.softDelete(id)
                                    expirationScheduler.cancelExpiration(id)
                                }
                            }

                            if (activeParties.isNotEmpty()) {
                                partyDao.insertAll(activeParties)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error processing snapshot", e)
                        }
                    }
                }
            }
    }

    private fun stopSyncing() {
        Log.d(TAG, "Stopping real-time Firestore sync")
        partyListener?.remove()
        partyListener = null
        isSyncing = false
    }

    override suspend fun createParty(name: String, expiresAt: Instant): Result<PartyEntity> {
        return try {
            val result = partyService.createParty(name, expiresAt.toEpochMilliseconds())
            result.fold(
                onSuccess = { data ->
                    @Suppress("UNCHECKED_CAST")
                    val partyMap = data["party"] as Map<String, Any>
                    val id = partyMap["id"] as String
                    val partyName = partyMap["name"] as String
                    val hostUserId = partyMap["hostUserId"] as String
                    val createdAtStr = partyMap["createdAt"] as String
                    val expiresAtStr = partyMap["expiresAt"] as String

                    val createdAt = Instant.parse(createdAtStr)
                    val expiresAtParsed = Instant.parse(expiresAtStr)

                    val partyEntity = PartyEntity(
                        id = id,
                        name = partyName,
                        hostUserId = hostUserId,
                        createdAt = createdAt,
                        expiresAt = expiresAtParsed,
                        isDeleted = false
                    )

                    // Save to Room cache
                    partyDao.insert(partyEntity)

                    // Schedule local expiration
                    expirationScheduler.scheduleExpiration(id, expiresAtParsed)

                    Result.success(partyEntity)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun joinParty(partyId: String): Result<PartyEntity> {
        return try {
            val result = partyService.joinParty(partyId)
            result.fold(
                onSuccess = { data ->
                    @Suppress("UNCHECKED_CAST")
                    val partyMap = data["party"] as Map<String, Any>
                    val id = partyMap["id"] as String
                    val partyName = partyMap["name"] as String
                    val hostUserId = partyMap["hostUserId"] as String
                    val createdAtStr = partyMap["createdAt"] as String
                    val expiresAtStr = partyMap["expiresAt"] as String

                    val createdAt = Instant.parse(createdAtStr)
                    val expiresAtParsed = Instant.parse(expiresAtStr)

                    val partyEntity = PartyEntity(
                        id = id,
                        name = partyName,
                        hostUserId = hostUserId,
                        createdAt = createdAt,
                        expiresAt = expiresAtParsed,
                        isDeleted = false
                    )

                    // Save to Room cache
                    partyDao.insert(partyEntity)

                    // Schedule local expiration
                    expirationScheduler.scheduleExpiration(id, expiresAtParsed)

                    Result.success(partyEntity)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun fetchUserParties(): Flow<List<PartyEntity>> {
        return partyDao.getActiveParties(Clock.System.now())
    }

    override suspend fun deleteParty(partyId: String): Result<Unit> {
        return try {
            // Soft delete locally first
            partyDao.softDelete(partyId)
            expirationScheduler.cancelExpiration(partyId)

            // Leave/Delete on backend via function
            partyService.leaveParty(partyId)
            
            // If current user is host, mark inactive in Firestore
            val currentUserId = auth.currentUser?.uid
            if (currentUserId != null) {
                val partyDoc = firestore.collection("parties").document(partyId).get().await()
                if (partyDoc.exists() && partyDoc.getString("hostUserId") == currentUserId) {
                    firestore.collection("parties").document(partyId).update("isActive", false).await()
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getPartyById(partyId: String): Flow<PartyEntity?> {
        return partyDao.getPartyById(partyId)
    }
}
