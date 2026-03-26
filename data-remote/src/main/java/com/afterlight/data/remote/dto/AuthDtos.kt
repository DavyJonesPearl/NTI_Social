package com.afterlight.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Authentication DTOs.
 * Stage 13: Login, register, refresh token requests/responses.
 */

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("displayName")
    val displayName: String?
)

data class RegisterRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("displayName")
    val displayName: String?
)

data class RegisterResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("displayName")
    val displayName: String?
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class RefreshTokenResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
