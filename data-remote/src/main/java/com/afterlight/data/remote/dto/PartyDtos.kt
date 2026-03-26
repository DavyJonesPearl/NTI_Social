package com.afterlight.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant

/**
 * Party DTOs.
 * Stage 13: Create party, join party, party details requests/responses.
 */

data class CreatePartyRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("durationMinutes")
    val durationMinutes: Int = 60
)

data class PartyResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("hostUserId")
    val hostUserId: String,
    @SerializedName("createdAt")
    val createdAt: Instant,
    @SerializedName("expiresAt")
    val expiresAt: Instant,
    @SerializedName("isDeleted")
    val isDeleted: Boolean = false
)

data class JoinPartyResponse(
    @SerializedName("party")
    val party: PartyResponse,
    @SerializedName("joinedAt")
    val joinedAt: Instant
)
