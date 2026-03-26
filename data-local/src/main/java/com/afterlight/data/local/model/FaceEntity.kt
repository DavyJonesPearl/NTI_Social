package com.afterlight.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Face detection entity for local database.
 * Stage 13: Face tracking for blur functionality.
 */
@Entity(
    tableName = "faces",
    foreignKeys = [
        ForeignKey(
            entity = MediaEntity::class,
            parentColumns = ["id"],
            childColumns = ["mediaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("mediaId")]
)
data class FaceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mediaId: String,
    val boundingBox: String, // JSON format: {"x":0,"y":0,"width":100,"height":100}
    val isBlurred: Boolean = false
)
