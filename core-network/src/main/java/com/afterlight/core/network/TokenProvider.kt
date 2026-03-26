package com.afterlight.core.network

/**
 * Token provider interface for JWT authentication.
 * Stage 13: Implemented by feature-auth module (SecureTokenProvider).
 */
interface TokenProvider {
    
    /**
     * Returns the current access token, or null if not authenticated.
     */
    fun getAccessToken(): String?
    
    /**
     * Returns the current refresh token, or null if not available.
     */
    fun getRefreshToken(): String?
    
    /**
     * Attempts to refresh the access token using the refresh token.
     * 
     * @return Result.success(newAccessToken) or Result.failure(exception)
     */
    suspend fun refreshAccessToken(): Result<String>
    
    /**
     * Clears all stored tokens (logout).
     */
    fun clearTokens()
}
