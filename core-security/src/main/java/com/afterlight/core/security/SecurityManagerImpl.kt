package com.afterlight.core.security

import android.security.keystore.KeyProperties
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AES-256-GCM file encryption.
 *
 * New captures use the shared party media key so every member can decrypt.
 * Legacy local files encrypted with a per-device Keystore key remain decryptable
 * on the capturing device only.
 */
@Singleton
class SecurityManagerImpl @Inject constructor(
    private val partyKeyStore: PartyKeyStore
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
                val secretKey = getSharedKeyOrThrow(partyId)
                encryptWithKey(inputFile, outputFile, secretKey)
                Log.d(TAG, "Encrypted file: ${inputFile.name} -> ${outputFile.name}")
            } catch (e: Exception) {
                Log.e(TAG, "Encryption failed for party $partyId", e)
                throw SecurityException("Encryption failed: ${e.message}", e)
            }
        }
    }
    
    override suspend fun decryptFile(inputFile: File, outputFile: File, partyId: String) {
        withContext(Dispatchers.IO) {
            val keys = decryptionKeys(partyId)
            if (keys.isEmpty()) {
                throw SecurityException("No decryption key available for party $partyId")
            }

            var lastError: Exception? = null
            for (secretKey in keys) {
                try {
                    decryptWithKey(inputFile, outputFile, secretKey)
                    Log.d(TAG, "Decrypted file: ${inputFile.name} -> ${outputFile.name}")
                    return@withContext
                } catch (e: Exception) {
                    lastError = e
                    if (outputFile.exists()) {
                        outputFile.delete()
                    }
                }
            }

            Log.e(TAG, "Decryption failed for party $partyId", lastError)
            throw SecurityException("Decryption failed: ${lastError?.message}", lastError)
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
                partyKeyStore.deleteKey(partyId)
                val keyAlias = "$KEY_ALIAS_PREFIX$partyId"
                if (keyStore.containsAlias(keyAlias)) {
                    keyStore.deleteEntry(keyAlias)
                }
                Log.d(TAG, "Deleted encryption keys for party: $partyId")
            } catch (e: Exception) {
                Log.e(TAG, "Key rotation failed for party $partyId", e)
                throw SecurityException("Key rotation failed: ${e.message}", e)
            }
        }
    }

    private fun getSharedKeyOrThrow(partyId: String): SecretKey {
        val shared = partyKeyStore.getKey(partyId)
            ?: throw SecurityException("Shared party media key is not available yet")
        return SecretKeySpec(shared, KeyProperties.KEY_ALGORITHM_AES)
    }

    private fun decryptionKeys(partyId: String): List<SecretKey> {
        val keys = mutableListOf<SecretKey>()
        partyKeyStore.getKey(partyId)?.let {
            keys.add(SecretKeySpec(it, KeyProperties.KEY_ALGORITHM_AES))
        }
        getLegacyKeystoreKey(partyId)?.let { keys.add(it) }
        return keys
    }

    private fun getLegacyKeystoreKey(partyId: String): SecretKey? {
        val keyAlias = "$KEY_ALIAS_PREFIX$partyId"
        if (!keyStore.containsAlias(keyAlias)) {
            return null
        }
        return runCatching {
            val entry = keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry
            entry.secretKey
        }.getOrNull()
    }

    private fun encryptWithKey(inputFile: File, outputFile: File, secretKey: SecretKey) {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        val iv = ByteArray(IV_LENGTH_BYTES)
        SecureRandom().nextBytes(iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv))

        FileInputStream(inputFile).use { input ->
            FileOutputStream(outputFile).use { output ->
                output.write(iv)
                val buffer = ByteArray(BUFFER_SIZE)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    val encryptedChunk = cipher.update(buffer, 0, bytesRead)
                    if (encryptedChunk != null) {
                        output.write(encryptedChunk)
                    }
                }
                output.write(cipher.doFinal())
                buffer.fill(0)
            }
        }
    }

    private fun decryptWithKey(inputFile: File, outputFile: File, secretKey: SecretKey) {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        FileInputStream(inputFile).use { input ->
            val iv = ByteArray(IV_LENGTH_BYTES)
            val ivBytesRead = input.read(iv)
            if (ivBytesRead != IV_LENGTH_BYTES) {
                throw SecurityException("Invalid encrypted file: IV missing or truncated")
            }
            cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv))
            FileOutputStream(outputFile).use { output ->
                val buffer = ByteArray(BUFFER_SIZE)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    val decryptedChunk = cipher.update(buffer, 0, bytesRead)
                    if (decryptedChunk != null) {
                        output.write(decryptedChunk)
                    }
                }
                val finalBlock = cipher.doFinal()
                if (finalBlock.isNotEmpty()) {
                    output.write(finalBlock)
                }
                buffer.fill(0)
            }
        }
    }
}
