package com.afterlight.feature.camera.domain

import android.content.Context
import androidx.camera.core.ImageProxy
import com.afterlight.core.security.SecurityManager
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.data.local.model.MediaEntity
import com.afterlight.data.remote.firebase.FirebaseMediaService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.nio.ByteBuffer
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Camera repository with encrypted capture.
 * Stage 13: ImageProxy → ByteArray → Encrypt → Room → Upload.
 */
@Singleton
class CameraRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val securityManager: SecurityManager,
    private val mediaDao: MediaDao,
    private val firebaseMediaService: FirebaseMediaService
) {
    
    /**
     * Captures photo from ImageProxy, encrypts with per-party key, saves to Room.
     * Returns MediaEntity on success.
     */
    suspend fun capturePhoto(partyId: String, imageProxy: ImageProxy): Result<MediaEntity> {
        var rawBytes: ByteArray? = null
        
        return try {
            val mediaId = UUID.randomUUID().toString()
            
            // Convert ImageProxy to ByteArray
            rawBytes = imageProxyToByteArray(imageProxy)
            
            // Write raw bytes to temp file
            val rawFile = File(context.cacheDir, "temp_raw_$mediaId.jpg")
            rawFile.writeBytes(rawBytes)
            
            // Encrypt to final location
            val encryptedFile = File(context.filesDir, "parties/$partyId/$mediaId.enc")
            encryptedFile.parentFile?.mkdirs()
            
            securityManager.encryptFile(rawFile, encryptedFile, partyId)
            
            // Delete raw temp file immediately
            rawFile.delete()
            
            // Save to Room database
            val mediaEntity = MediaEntity(
                id = mediaId,
                partyId = partyId,
                encryptedFilePath = encryptedFile.absolutePath,
                createdAt = Clock.System.now(),
                flagged = false
            )
            mediaDao.insert(mediaEntity)
            
            // Upload encrypted file to backend (async, don't block capture)
            uploadMediaAsync(partyId, mediaId, encryptedFile)
            
            Result.success(mediaEntity)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            // Zero out raw bytes in memory
            rawBytes?.fill(0)
        }
    }
    
    /**
     * Converts ImageProxy to ByteArray (JPEG format).
     */
    private fun imageProxyToByteArray(imageProxy: ImageProxy): ByteArray {
        val buffer: ByteBuffer = imageProxy.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return bytes
    }
    
    /**
     * Uploads encrypted media file to backend (fire-and-forget).
     */
    private fun uploadMediaAsync(partyId: String, mediaId: String, encryptedFile: File) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                firebaseMediaService.uploadMedia(partyId, mediaId, encryptedFile)
            } catch (e: Exception) {
                // Log but don't fail capture
                android.util.Log.e("CameraRepository", "Upload failed: ${e.message}")
            }
        }
    }
}
