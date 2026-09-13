package com.afterlight.feature.camera.di

import com.afterlight.feature.camera.controller.CameraController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Camera module for Hilt dependency injection.
 * CameraRepository is constructor-injected.
 */
@Module
@InstallIn(SingletonComponent::class)
object CameraModule {
    
    @Provides
    @Singleton
    fun provideCameraController(): CameraController {
        return CameraController()
    }
}
