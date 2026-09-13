package com.afterlight.data.local.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import java.util.Base64

/**
 * Persists the SQLCipher database passphrase in EncryptedSharedPreferences.
 * Generates a random 32-byte passphrase on first run.
 */
class SqlCipherPassphraseStore(context: Context) {
    private companion object {
        const val PREFS_NAME = "afterlight_db_passphrase"
        const val KEY_PASSPHRASE = "passphrase"
        const val PASSPHRASE_BYTES = 32
        val LEGACY_PASSPHRASE: ByteArray = "afterlight_encryption_key_2024".toByteArray()
    }

    private val prefs: SharedPreferences

    init {
        val masterKey = MasterKey.Builder(context.applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        prefs = EncryptedSharedPreferences.create(
            context.applicationContext,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun getOrCreatePassphrase(): ByteArray {
        val existing = prefs.getString(KEY_PASSPHRASE, null)
        if (existing != null) {
            return Base64.getDecoder().decode(existing)
        }
        val generated = ByteArray(PASSPHRASE_BYTES)
        SecureRandom().nextBytes(generated)
        prefs.edit()
            .putString(KEY_PASSPHRASE, Base64.getEncoder().encodeToString(generated))
            .apply()
        return generated
    }

    fun legacyPassphrase(): ByteArray = LEGACY_PASSPHRASE.copyOf()
}
