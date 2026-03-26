package com.afterlight.feature.party.domain

import com.afterlight.data.local.dao.PartyDao
import com.afterlight.data.local.model.PartyEntity
import com.afterlight.data.remote.api.PartyApi
import com.afterlight.data.remote.dto.CreatePartyRequest
import com.afterlight.feature.party.worker.PartyExpirationScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Party repository with remote-first writes and Room sync.
 * Stage 13: Party lifecycle management with expiration tracking.
 */
@Singleton
class PartyRepository @Inject constructor(
    private val partyApi: PartyApi,
    private val partyDao: PartyDao,
    private val expirationScheduler: PartyExpirationScheduler
) {
    
    /**
     * Creates a new party (remote-first, then syncs to local).
     */
    suspend fun createParty(name: String, expiresAt: Instant): Result<PartyEntity> {
        return try {
            val durationMinutes = ((expiresAt - Clock.System.now()).inWholeMinutes).toInt()
            val response = partyApi.createParty(
                CreatePartyRequest(name = name, durationMinutes = durationMinutes)
            )
            
            if (response.isSuccessful && response.body() != null) {
                val partyResponse = response.body()!!
                
                val partyEntity = PartyEntity(
                    id = partyResponse.id,
                    name = partyResponse.name,
                    hostUserId = partyResponse.hostUserId,
                    createdAt = partyResponse.createdAt,
                    expiresAt = partyResponse.expiresAt,
                    isDeleted = false
                )
                
                // Sync to local database
                partyDao.insert(partyEntity)
                
                // Schedule expiration worker
                expirationScheduler.scheduleExpiration(partyResponse.id, partyResponse.expiresAt)
                
                Result.success(partyEntity)
            } else {
                Result.failure(Exception("Create party failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Joins an existing party (remote-first, then syncs to local).
     */
    suspend fun joinParty(partyId: String): Result<PartyEntity> {
        return try {
            val response = partyApi.joinParty(partyId)
            
            if (response.isSuccessful && response.body() != null) {
                val joinResponse = response.body()!!
                val partyResponse = joinResponse.party
                
                val partyEntity = PartyEntity(
                    id = partyResponse.id,
                    name = partyResponse.name,
                    hostUserId = partyResponse.hostUserId,
                    createdAt = partyResponse.createdAt,
                    expiresAt = partyResponse.expiresAt,
                    isDeleted = false
                )
                
                // Sync to local database
                partyDao.insert(partyEntity)
                
                // Schedule expiration worker
                expirationScheduler.scheduleExpiration(partyResponse.id, partyResponse.expiresAt)
                
                Result.success(partyEntity)
            } else {
                Result.failure(Exception("Join party failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Fetches active parties for current user (local-first with reactive updates).
     */
    fun fetchUserParties(): Flow<List<PartyEntity>> {
        return partyDao.getActiveParties(Clock.System.now())
    }
    
    /**
     * Deletes a party (remote-first, then soft deletes locally).
     */
    suspend fun deleteParty(partyId: String): Result<Unit> {
        return try {
            // Soft delete locally first
            partyDao.softDelete(partyId)
            
            // Cancel expiration worker
            expirationScheduler.cancelExpiration(partyId)
            
            // Then notify backend
            val response = partyApi.deleteParty(partyId)
            
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Delete party failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Gets a single party by ID (local-first).
     */
    fun getPartyById(partyId: String): Flow<PartyEntity?> {
        return partyDao.getPartyById(partyId)
    }
}
