package com.afterlight.feature.gallery.di

import com.afterlight.core.security.SecurityManager
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.feature.gallery.domain.GalleryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Gallery module for Hilt dependency injection.
 * Stage 13: Provides GalleryRepository singleton.
 */
@Module
@InstallIn(SingletonComponent::class)
object GalleryModule {
    
    @Provides
    @Singleton
    fun provideGalleryRepository(
        securityManager: SecurityManager,
        mediaDao: MediaDao
    ): GalleryRepository {
        return GalleryRepository(securityManager, mediaDao)
    }
}

/**
 * EntryPoint for accessing GalleryRepository from Composables.
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface GalleryEntryPoint {
    fun repository(): GalleryRepository
}
