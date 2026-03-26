package com.afterlight.core.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Production implementation of SecurityManager using Android Keystore.
 * Stage 13: AES-256-GCM with 128-bit GCM tag, 12-byte IV.
 */
@Singleton
class SecurityManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SecurityManager {
    
    private companion object {
        const val TAG = "SecurityManagerImpl"
        const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        const val KEY_ALIAS_PREFIX = "afterlight_party_"
        const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
        const val IV_LENGTH_BYTES = 12
        const val GCM_TAG_LENGTH_BITS = 128
        const val BUFFER_SIZE = 8192
    }
    
    private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
        load(null)
    }
    
    override suspend fun encryptFile(inputFile: File, outputFile: File, partyId: String) {
        withContext(Dispatchers.IO) {
            try {
                val secretKey = getOrCreateKeyForParty(partyId)
                val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
                
                // Generate random IV (12 bytes for GCM)
                val iv = ByteArray(IV_LENGTH_BYTES)
                SecureRandom().nextBytes(iv)
                
                val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)
                
                FileInputStream(inputFile).use { input ->
                    FileOutputStream(outputFile).use { output ->
                        // Write IV first (12 bytes)
                        output.write(iv)
                        
                        // Encrypt file contents
                        val buffer = ByteArray(BUFFER_SIZE)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            val encryptedChunk = cipher.update(buffer, 0, bytesRead)
                            if (encryptedChunk != null) {
                                output.write(encryptedChunk)
                            }
                        }
                        
                        // Write final block (includes GCM tag)
                        val finalBlock = cipher.doFinal()
                        output.write(finalBlock)
                        
                        // Wipe buffer
                        buffer.fill(0)
                    }
                }
                
                Log.d(TAG, "Encrypted file: ${inputFile.name} -> ${outputFile.name} (party: $partyId)")
            } catch (e: Exception) {
                Log.e(TAG, "Encryption failed for party $partyId", e)
                throw SecurityException("Encryption failed: ${e.message}", e)
            }
        }
    }
    
    override suspend fun decryptFile(inputFile: File, outputFile: File, partyId: String) {
        withContext(Dispatchers.IO) {
            try {
                val secretKey = getOrCreateKeyForParty(partyId)
                val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
                
                FileInputStream(inputFile).use { input ->
                    // Read IV from first 12 bytes
                    val iv = ByteArray(IV_LENGTH_BYTES)
                    val ivBytesRead = input.read(iv)
                    if (ivBytesRead != IV_LENGTH_BYTES) {
                        throw SecurityException("Invalid encrypted file: IV missing or truncated")
                    }
                    
                    val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)
                    
                    FileOutputStream(outputFile).use { output ->
                        val buffer = ByteArray(BUFFER_SIZE)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            val decryptedChunk = cipher.update(buffer, 0, bytesRead)
                            if (decryptedChunk != null) {
                                output.write(decryptedChunk)
                            }
                        }
                        
                        // Finalize decryption (verifies GCM tag)
                        val finalBlock = cipher.doFinal()
                        if (finalBlock.isNotEmpty()) {
                            output.write(finalBlock)
                        }
                        
                        // Wipe buffer
                        buffer.fill(0)
                    }
                }
                
                Log.d(TAG, "Decrypted file: ${inputFile.name} -> ${outputFile.name} (party: $partyId)")
            } catch (e: Exception) {
                Log.e(TAG, "Decryption failed for party $partyId", e)
                // Clean up partial output
                if (outputFile.exists()) {
                    outputFile.delete()
                }
                throw SecurityException("Decryption failed: ${e.message}", e)
            }
        }
    }
    
    override suspend fun deleteSecurely(file: File): Boolean = withContext(Dispatchers.IO) {
        if (!file.exists()) {
            Log.w(TAG, "File does not exist: ${file.absolutePath}")
            return@withContext false
        }
        
        try {
            file.secureDelete()
            Log.d(TAG, "Securely deleted: ${file.name}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Secure deletion failed: ${file.name}", e)
            false
        }
    }
    
    override suspend fun rotatePartyKey(partyId: String) {
        withContext(Dispatchers.IO) {
            try {
                val keyAlias = "$KEY_ALIAS_PREFIX$partyId"
                
                // Delete existing key
                if (keyStore.containsAlias(keyAlias)) {
                    keyStore.deleteEntry(keyAlias)
                    Log.d(TAG, "Deleted existing key for party: $partyId")
                }
                
                // Generate new key
                getOrCreateKeyForParty(partyId)
                Log.d(TAG, "Rotated key for party: $partyId")
            } catch (e: Exception) {
                Log.e(TAG, "Key rotation failed for party $partyId", e)
                throw SecurityException("Key rotation failed: ${e.message}", e)
            }
        }
    }
    
    /**
     * Retrieves existing key or generates new AES-256 key in Android Keystore.
     */
    private fun getOrCreateKeyForParty(partyId: String): SecretKey {
        val keyAlias = "$KEY_ALIAS_PREFIX$partyId"
        
        // Check if key exists
        if (keyStore.containsAlias(keyAlias)) {
            val entry = keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry
            return entry.secretKey
        }
        
        // Generate new key
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE_PROVIDER
        )
        
        val keySpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(false)
            .build()
        
        keyGenerator.init(keySpec)
        val secretKey = keyGenerator.generateKey()
        
        Log.d(TAG, "Generated new AES-256 key for party: $partyId")
        return secretKey
    }
}
