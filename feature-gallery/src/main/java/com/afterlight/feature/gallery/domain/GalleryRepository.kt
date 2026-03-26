package com.afterlight.feature.gallery.domain

import com.afterlight.core.security.SecurityManager
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.data.local.model.MediaEntity
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Gallery repository with memory-safe decryption.
 * Stage 13: Flow-based media list, on-demand decryption, GCM tag handling.
 */
@Singleton
class GalleryRepository @Inject constructor(
    private val securityManager: SecurityManager,
    private val mediaDao: MediaDao
) {
    
    /**
     * Gets reactive Flow of media for a party from Room.
     * Returns Flow that updates automatically when database changes.
     */
    fun getMediaForParty(partyId: String): Flow<List<MediaEntity>> {
        return mediaDao.getMediaForParty(partyId)
    }
    
    /**
     * Decrypts a single media file to ByteArray for display.
     * Returns decrypted bytes in memory (caller must zero after use).
     * 
     * GCM tag failures indicate:
     * - File corruption
     * - Wrong encryption key
     * - Tampering attempt
     */
    suspend fun decryptMedia(mediaId: String, partyId: String): Result<ByteArray> {
        return try {
            // Get media entity from Room
            var mediaEntity: MediaEntity? = null
            mediaDao.getMediaById(mediaId).collect { entity ->
                mediaEntity = entity
            }
            
            if (mediaEntity == null) {
                return Result.failure(Exception("Media not found"))
            }
            
            val encryptedFile = File(mediaEntity!!.encryptedFilePath)
            if (!encryptedFile.exists()) {
                return Result.failure(Exception("Encrypted file not found"))
            }
            
            // Decrypt to temp file (SecurityManager doesn't support ByteArray output directly)
            val tempDecrypted = File.createTempFile("decrypt_", ".jpg")
            
            try {
                securityManager.decryptFile(encryptedFile, tempDecrypted, partyId)
                
                // Read decrypted bytes
                val bytes = tempDecrypted.readBytes()
                
                Result.success(bytes)
            } catch (e: SecurityException) {
                // GCM tag verification failed - file corrupted or tampered
                Result.failure(Exception("Decryption failed: GCM tag mismatch (corrupted file)", e))
            } finally {
                // Always delete temp file
                tempDecrypted.delete()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
