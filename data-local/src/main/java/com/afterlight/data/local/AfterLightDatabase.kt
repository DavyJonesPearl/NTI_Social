package com.afterlight.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.afterlight.data.local.converter.InstantConverter
import com.afterlight.data.local.converter.SyncStatusConverter
import com.afterlight.data.local.dao.FaceDao
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.data.local.dao.PartyDao
import com.afterlight.data.local.dao.SyncStateDao
import com.afterlight.data.local.dao.UserDao
import com.afterlight.data.local.model.FaceEntity
import com.afterlight.data.local.model.MediaEntity
import com.afterlight.data.local.model.PartyEntity
import com.afterlight.data.local.model.SyncStateEntity
import com.afterlight.data.local.model.UserEntity

/**
 * AfterLight Room database.
 * Stage 13: 5 entities with encrypted storage via SQLCipher.
 */
@Database(
    entities = [
        UserEntity::class,
        PartyEntity::class,
        MediaEntity::class,
        FaceEntity::class,
        SyncStateEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    InstantConverter::class,
    SyncStatusConverter::class
)
abstract class AfterLightDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun partyDao(): PartyDao
    abstract fun mediaDao(): MediaDao
    abstract fun faceDao(): FaceDao
    abstract fun syncStateDao(): SyncStateDao
    
    companion object {
        const val DATABASE_NAME = "afterlight_db"
    }
}
