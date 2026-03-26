package com.afterlight.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * Party entity for local database.
 * Stage 13: Party lifecycle with expiration tracking.
 */
@Entity(tableName = "parties")
data class PartyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val hostUserId: String,
    val createdAt: Instant,
    val expiresAt: Instant,
    val isDeleted: Boolean = false
)
