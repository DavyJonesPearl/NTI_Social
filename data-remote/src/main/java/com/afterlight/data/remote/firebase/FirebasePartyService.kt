package com.afterlight.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service to interact with Firebase Party Cloud Functions and Firestore.
 */
@Singleton
class FirebasePartyService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions,
    private val auth: FirebaseAuth
) {
    
    suspend fun createParty(name: String, expiresAtMs: Long): Result<Map<String, Any>> {
        return try {
            val data = hashMapOf(
                "name" to name,
                "expiresAt" to expiresAtMs
            )
            val result = functions.getHttpsCallable("createParty").call(data).await()
            @Suppress("UNCHECKED_CAST")
            val resultData = result.getData() as? Map<String, Any>
            if (resultData != null && resultData["success"] == true) {
                Result.success(resultData)
            } else {
                Result.failure(Exception("Failed to create party on server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinParty(partyId: String): Result<Map<String, Any>> {
        return try {
            val data = hashMapOf(
                "partyId" to partyId
            )
            val result = functions.getHttpsCallable("joinParty").call(data).await()
            @Suppress("UNCHECKED_CAST")
            val resultData = result.getData() as? Map<String, Any>
            if (resultData != null && resultData["success"] == true) {
                Result.success(resultData)
            } else {
                Result.failure(Exception("Failed to join party on server"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun leaveParty(partyId: String): Result<Unit> {
        return try {
            val data = hashMapOf(
                "partyId" to partyId
            )
            functions.getHttpsCallable("leaveParty").call(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
