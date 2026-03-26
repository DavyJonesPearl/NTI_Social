package com.afterlight.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant

/**
 * Media DTOs.
 * Stage 13: Upload media, media metadata requests/responses.
 */

data class MediaResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("partyId")
    val partyId: String,
    @SerializedName("uploadedBy")
    val uploadedBy: String,
    @SerializedName("createdAt")
    val createdAt: Instant,
    @SerializedName("flagged")
    val flagged: Boolean = false,
    @SerializedName("downloadUrl")
    val downloadUrl: String?
)
