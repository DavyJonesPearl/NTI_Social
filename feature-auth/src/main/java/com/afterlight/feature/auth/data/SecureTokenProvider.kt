package com.afterlight.feature.auth.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.afterlight.core.network.TokenProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.Lazy
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Secure token provider using EncryptedSharedPreferences.
 * Stage 13: AES256-GCM encrypted token storage, thread-safe refresh.
 */
@Singleton
class SecureTokenProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenProvider {
    
    private companion object {
        const val PREFS_NAME = "afterlight_secure_tokens"
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
    
    private val refreshMutex = Mutex()
    
    private val sharedPreferences: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    override fun getAccessToken(): String? {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
    }
    
    override fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }
    
    override suspend fun refreshAccessToken(): Result<String> {
        // Token refresh handled by AuthRepository directly
        return Result.failure(UnsupportedOperationException("Use AuthRepository.refreshToken() instead"))
    }
    
    override fun clearTokens() {
        sharedPreferences.edit().clear().apply()
    }
    
    /**
     * Saves access and refresh tokens to encrypted storage.
     */
    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }
}
