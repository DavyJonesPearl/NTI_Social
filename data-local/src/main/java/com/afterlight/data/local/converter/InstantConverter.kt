package com.afterlight.data.local.converter

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

/**
 * Room type converter for kotlinx.datetime.Instant.
 * Stage 13: Instant stored as epoch milliseconds (Long).
 */
class InstantConverter {
    
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(it) }
    }
    
    @TypeConverter
    fun toTimestamp(instant: Instant?): Long? {
        return instant?.toEpochMilliseconds()
    }
}
