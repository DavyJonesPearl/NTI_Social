package com.afterlight.feature.party.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Scheduler for party expiration workers.
 * Stage 13: WorkManager with exponential backoff.
 */
@Singleton
class PartyExpirationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val workManager = WorkManager.getInstance(context)
    
    /**
     * Schedules expiration cleanup for a party.
     * 
     * @param partyId Party identifier
     * @param expiresAt Expiration timestamp
     */
    fun scheduleExpiration(partyId: String, expiresAt: Instant) {
        val currentTime = System.currentTimeMillis()
        val expirationTime = expiresAt.toEpochMilliseconds()
        val delayMillis = (expirationTime - currentTime).coerceAtLeast(0)
        
        val inputData = Data.Builder()
            .putString(PartyExpirationWorker.KEY_PARTY_ID, partyId)
            .build()
        
        val workRequest = OneTimeWorkRequestBuilder<PartyExpirationWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                30, // Initial backoff 30 seconds
                TimeUnit.SECONDS
            )
            .build()
        
        // Use ExistingWorkPolicy.REPLACE to update if party expiration changes
        workManager.enqueueUniqueWork(
            "party_expiration_$partyId",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    /**
     * Cancels scheduled expiration for a party.
     */
    fun cancelExpiration(partyId: String) {
        workManager.cancelUniqueWork("party_expiration_$partyId")
    }
}
