package com.afterlight.feature.party.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.afterlight.core.security.SecurityManager
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.data.local.dao.PartyDao
import com.afterlight.feature.party.domain.PartyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.io.File

/**
 * Worker for party expiration cleanup.
 * Stage 13: Secure file deletion, Keystore key rotation, soft delete cascade.
 */
@HiltWorker
class PartyExpirationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val partyDao: PartyDao,
    private val mediaDao: MediaDao,
    private val partyRepository: PartyRepository,
    private val securityManager: SecurityManager
) : CoroutineWorker(context, params) {
    
    companion object {
        private const val TAG = "PartyExpirationWorker"
        const val KEY_PARTY_ID = "party_id"
    }
    
    override suspend fun doWork(): Result {
        val partyId = inputData.getString(KEY_PARTY_ID)
            ?: return Result.failure()
        
        Log.d(TAG, "Starting expiration cleanup for party: $partyId")
        
        return try {
            // 1. Get all media for party
            val mediaList = mediaDao.getMediaForParty(partyId).first()
            Log.d(TAG, "Found ${mediaList.size} media files to delete")
            
            // 2. Securely delete encrypted files
            mediaList.forEach { media ->
                val file = File(media.encryptedFilePath)
                if (file.exists()) {
                    try {
                        securityManager.deleteSecurely(file)
                        Log.d(TAG, "Securely deleted: ${media.id}")
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to delete file: ${media.id}", e)
                    }
                }
            }
            
            // 3. Rotate party encryption key (deletes old key)
            try {
                securityManager.rotatePartyKey(partyId)
                Log.d(TAG, "Rotated encryption key for party: $partyId")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to rotate key for party: $partyId", e)
            }
            
            // 4. Soft delete party (cascades to media/faces/sync_state via foreign keys)
            partyDao.softDelete(partyId)
            Log.d(TAG, "Soft deleted party: $partyId")
            
            // 5. Sync deletion to backend (best effort)
            try {
                partyRepository.deleteParty(partyId)
                Log.d(TAG, "Synced deletion to backend: $partyId")
            } catch (e: Exception) {
                Log.w(TAG, "Failed to sync deletion to backend: $partyId", e)
                // Don't fail the worker for backend sync errors
            }
            
            Log.d(TAG, "Completed expiration cleanup for party: $partyId")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Expiration cleanup failed for party: $partyId", e)
            Result.retry()
        }
    }
}
