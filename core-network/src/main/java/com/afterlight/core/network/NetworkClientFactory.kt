package com.afterlight.core.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Factory for creating configured OkHttpClient instances.
 * Stage 13: JWT interceptor + logging + timeouts.
 */
@Singleton
class NetworkClientFactory @Inject constructor() {
    
    companion object {
        private const val TAG = "NetworkClientFactory"
        private const val CONNECT_TIMEOUT_SECONDS = 30L
        private const val READ_TIMEOUT_SECONDS = 30L
        private const val WRITE_TIMEOUT_SECONDS = 30L
    }
    
    /**
     * Creates a production OkHttpClient with JWT authentication.
     * 
     * @param tokenProvider Token provider for JWT authentication
     * @param enableLogging Enable HTTP logging (debug builds only)
     */
    fun createOkHttpClient(
        tokenProvider: TokenProvider?,
        enableLogging: Boolean = false
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .apply {
                // Add JWT interceptor if token provider available
                if (tokenProvider != null) {
                    addInterceptor(createJwtInterceptor(tokenProvider))
                }
                
                // Add logging interceptor for debug builds
                if (enableLogging) {
                    addInterceptor(createLoggingInterceptor())
                }
            }
            .build()
    }
    
    /**
     * Creates JWT authorization interceptor.
     * Adds "Authorization: Bearer {token}" header to requests.
     */
    private fun createJwtInterceptor(tokenProvider: TokenProvider): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            
            val accessToken = tokenProvider.getAccessToken()
            
            val newRequest = if (accessToken != null) {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
            } else {
                originalRequest
            }
            
            chain.proceed(newRequest)
        }
    }
    
    /**
     * Creates HTTP logging interceptor for debug builds.
     * Logs headers and body (excludes sensitive authorization headers in production).
     */
    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d(TAG, message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }
}
