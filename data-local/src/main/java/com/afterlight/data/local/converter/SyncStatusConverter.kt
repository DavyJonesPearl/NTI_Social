package com.afterlight.data.local.converter

import androidx.room.TypeConverter
import com.afterlight.data.local.model.SyncStatus

/**
 * Room type converter for SyncStatus enum.
 * Stage 13: Enum stored as string name.
 */
class SyncStatusConverter {
    
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus?): String? {
        return status?.name
    }
    
    @TypeConverter
    fun toSyncStatus(value: String?): SyncStatus? {
        return value?.let { SyncStatus.valueOf(it) }
    }
}
