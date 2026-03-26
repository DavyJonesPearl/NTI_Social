package com.afterlight.data.local.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.afterlight.data.local.AfterLightDatabase
import com.afterlight.data.local.dao.FaceDao
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.data.local.dao.PartyDao
import com.afterlight.data.local.dao.SyncStateDao
import com.afterlight.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.zetetic.database.sqlcipher.SQLiteDatabase
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import javax.inject.Singleton

/**
 * Hilt module for data-local dependency injection.
 * Stage 13: SQLCipher encrypted database, singleton DAOs.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provides singleton AfterLightDatabase with SQLCipher encryption.
     * Passphrase should be retrieved from secure storage in production.
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AfterLightDatabase {
        // TODO: Retrieve passphrase from Android Keystore in production
        val passphrase = "afterlight_encryption_key_2024".toByteArray()
        val factory = SupportOpenHelperFactory(passphrase)
        
        return Room.databaseBuilder(
            context,
            AfterLightDatabase::class.java,
            AfterLightDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration() // MVP: Allow data loss on schema changes
            .build()
    }
    
    @Provides
    @Singleton
    fun provideUserDao(database: AfterLightDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    @Singleton
    fun providePartyDao(database: AfterLightDatabase): PartyDao {
        return database.partyDao()
    }
    
    @Provides
    @Singleton
    fun provideMediaDao(database: AfterLightDatabase): MediaDao {
        return database.mediaDao()
    }
    
    @Provides
    @Singleton
    fun provideFaceDao(database: AfterLightDatabase): FaceDao {
        return database.faceDao()
    }
    
    @Provides
    @Singleton
    fun provideSyncStateDao(database: AfterLightDatabase): SyncStateDao {
        return database.syncStateDao()
    }
}
