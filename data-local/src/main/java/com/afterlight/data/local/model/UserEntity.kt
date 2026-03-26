package com.afterlight.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * User entity for local database.
 * Stage 13: Room persistence layer.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val displayName: String?,
    val createdAt: Instant
)
