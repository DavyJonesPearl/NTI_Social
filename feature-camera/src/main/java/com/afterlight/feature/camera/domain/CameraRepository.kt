package com.afterlight.feature.camera.domain

import android.content.Context
import android.util.Log
import com.afterlight.core.security.PartyKeyStore
import com.afterlight.core.security.SecurityManager
import com.afterlight.data.local.MediaFilePaths
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.data.local.model.MediaEntity
import com.afterlight.data.remote.firebase.FirebaseMediaService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Camera repository with encrypted capture.
 * JPEG file → encrypt with shared party key → Room → Firebase upload.
 */
@Singleton
class CameraRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val securityManager: SecurityManager,
    private val partyKeyStore: PartyKeyStore,
    private val mediaDao: MediaDao,
    private val firebaseMediaService: FirebaseMediaService
) {
    private companion object {
        const val TAG = "CameraRepository"
    }
    
    /**
     * Encrypts a captured JPEG with the shared party key and persists it locally.
     */
    suspend fun capturePhoto(partyId: String, jpegFile: File): Result<MediaEntity> {
        return try {
            if (!jpegFile.exists() || jpegFile.length() == 0L) {
                return Result.failure(IllegalStateException("Captured photo is empty"))
            }
            if (!partyKeyStore.hasKey(partyId)) {
                return Result.failure(
                    IllegalStateException("Party encryption key is not ready. Re-open the party and try again.")
                )
            }

            val mediaId = UUID.randomUUID().toString()
            val encryptedFile = MediaFilePaths.encryptedFile(context.filesDir, partyId, mediaId)
            encryptedFile.parentFile?.mkdirs()
            
            securityManager.encryptFile(jpegFile, encryptedFile, partyId)
            
            val mediaEntity = MediaEntity(
                id = mediaId,
                partyId = partyId,
                encryptedFilePath = encryptedFile.absolutePath,
                createdAt = Clock.System.now(),
                flagged = false
            )
            mediaDao.insert(mediaEntity)
            uploadMediaAsync(partyId, mediaId, encryptedFile)
            
            Result.success(mediaEntity)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            if (jpegFile.exists()) {
                jpegFile.delete()
            }
        }
    }
    
    private fun uploadMediaAsync(partyId: String, mediaId: String, encryptedFile: File) {
        CoroutineScope(Dispatchers.IO).launch {
            repeat(3) { attempt ->
                val result = firebaseMediaService.uploadMedia(partyId, mediaId, encryptedFile)
                if (result.isSuccess) {
                    return@launch
                }
                Log.e(TAG, "Upload attempt ${attempt + 1} failed: ${result.exceptionOrNull()?.message}")
                if (attempt < 2) {
                    delay(2_000L * (attempt + 1))
                }
            }
        }
    }
}
