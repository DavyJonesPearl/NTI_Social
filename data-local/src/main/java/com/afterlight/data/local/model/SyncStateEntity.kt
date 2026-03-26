package com.afterlight.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

/**
 * Sync state entity for local database.
 * Stage 13: Network sync tracking.
 */
@Entity(
    tableName = "sync_state",
    foreignKeys = [
        ForeignKey(
            entity = MediaEntity::class,
            parentColumns = ["id"],
            childColumns = ["mediaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("mediaId"), Index("syncStatus")]
)
data class SyncStateEntity(
    @PrimaryKey
    val id: String,
    val mediaId: String,
    val syncStatus: SyncStatus,
    val lastAttemptAt: Instant?
)

enum class SyncStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
