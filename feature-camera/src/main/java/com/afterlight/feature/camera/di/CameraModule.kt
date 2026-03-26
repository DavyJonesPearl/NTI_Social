package com.afterlight.feature.camera.di

import android.content.Context
import com.afterlight.core.security.SecurityManager
import com.afterlight.data.local.dao.MediaDao
import com.afterlight.data.remote.api.MediaApi
import com.afterlight.feature.camera.controller.CameraController
import com.afterlight.feature.camera.domain.CameraRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Camera module for Hilt dependency injection.
 * Stage 13: Provides CameraRepository and CameraController singletons.
 */
@Module
@InstallIn(SingletonComponent::class)
object CameraModule {
    
    @Provides
    @Singleton
    fun provideCameraRepository(
        @ApplicationContext context: Context,
        securityManager: SecurityManager,
        mediaDao: MediaDao,
        mediaApi: MediaApi
    ): CameraRepository {
        return CameraRepository(context, securityManager, mediaDao, mediaApi)
    }
    
    @Provides
    @Singleton
    fun provideCameraController(): CameraController {
        return CameraController()
    }
}
