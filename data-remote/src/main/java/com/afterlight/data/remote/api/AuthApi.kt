package com.afterlight.data.remote.api

import com.afterlight.data.remote.dto.LoginRequest
import com.afterlight.data.remote.dto.LoginResponse
import com.afterlight.data.remote.dto.RefreshTokenRequest
import com.afterlight.data.remote.dto.RefreshTokenResponse
import com.afterlight.data.remote.dto.RegisterRequest
import com.afterlight.data.remote.dto.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Authentication API interface.
 * Stage 13: JWT-based authentication endpoints.
 */
interface AuthApi {
    
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
    
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
    
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<RefreshTokenResponse>
    
    @POST("auth/logout")
    suspend fun logout(): Response<Unit>
}
