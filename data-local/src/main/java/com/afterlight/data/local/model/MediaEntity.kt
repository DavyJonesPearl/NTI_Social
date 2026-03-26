package com.afterlight.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * Media entity for local database.
 * Stage 13: Encrypted file tracking with party association.
 */
@Entity(
    tableName = "media",
    foreignKeys = [
        ForeignKey(
            entity = PartyEntity::class,
            parentColumns = ["id"],
            childColumns = ["partyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("partyId")]
)
data class MediaEntity(
    @PrimaryKey
    val id: String,
    val partyId: String,
    val encryptedFilePath: String,
    val createdAt: Instant,
    val flagged: Boolean = false
)
