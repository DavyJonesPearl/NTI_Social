package com.afterlight.data.local.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.afterlight.data.local.AfterLightDatabase
import com.afterlight.data.local.dao.FaceDao
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.data.local.dao.PartyDao
import com.afterlight.data.local.dao.SyncStateDao
import com.afterlight.data.local.dao.UserDao
import com.afterlight.data.local.security.SqlCipherPassphraseStore
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
 * SQLCipher passphrase is generated per install and stored in EncryptedSharedPreferences.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val TAG = "DatabaseModule"
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AfterLightDatabase {
        val passphraseStore = SqlCipherPassphraseStore(context)
        val passphrase = passphraseStore.getOrCreatePassphrase()
        migrateLegacyPassphraseIfNeeded(context, passphrase, passphraseStore.legacyPassphrase())
        passphraseStore.legacyPassphrase().fill(0)

        val factory = SupportOpenHelperFactory(passphrase)
        
        return Room.databaseBuilder(
            context,
            AfterLightDatabase::class.java,
            AfterLightDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * One-time migration from the previous hardcoded passphrase to the stored random key.
     * If neither passphrase opens the existing file, the old database is removed.
     */
    private fun migrateLegacyPassphraseIfNeeded(
        context: Context,
        newPassphrase: ByteArray,
        legacyPassphrase: ByteArray
    ) {
        val dbFile = context.getDatabasePath(AfterLightDatabase.DATABASE_NAME)
        if (!dbFile.exists()) {
            return
        }

        if (canOpen(dbFile.absolutePath, newPassphrase)) {
            return
        }

        if (canOpen(dbFile.absolutePath, legacyPassphrase)) {
            try {
                val database = SQLiteDatabase.openOrCreateDatabase(
                    dbFile.absolutePath,
                    legacyPassphrase,
                    null,
                    null
                )
                database.changePassword(newPassphrase)
                database.close()
                Log.i(TAG, "Rekeyed SQLCipher database to install-specific passphrase")
                return
            } catch (e: Exception) {
                Log.w(TAG, "Failed to rekey legacy SQLCipher database", e)
            }
        }

        Log.w(TAG, "Unable to open existing SQLCipher database; recreating")
        context.deleteDatabase(AfterLightDatabase.DATABASE_NAME)
    }

    private fun canOpen(path: String, passphrase: ByteArray): Boolean {
        return try {
            val database = SQLiteDatabase.openOrCreateDatabase(path, passphrase, null, null)
            database.close()
            true
        } catch (_: Exception) {
            false
        }
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
