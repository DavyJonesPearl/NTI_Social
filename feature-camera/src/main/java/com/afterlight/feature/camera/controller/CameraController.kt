package com.afterlight.feature.camera.controller

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CameraX controller with lifecycle management.
 * Stage 13: Centralized camera setup, preview + capture binding.
 */
@Singleton
class CameraController @Inject constructor() {
    
    private var cameraProvider: ProcessCameraProvider? = null
    
    /**
     * Initializes CameraX with Preview and ImageCapture use cases.
     * Binds to lifecycle and returns ImageCapture instance.
     */
    suspend fun setupCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        surfaceProvider: Preview.SurfaceProvider
    ): Result<ImageCapture> {
        return try {
            val provider = ProcessCameraProvider.getInstance(context).get()
            cameraProvider = provider
            
            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(surfaceProvider)
            }
            
            val imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()
            
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            
            provider.unbindAll()
            provider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            
            Result.success(imageCapture)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Unbinds all camera use cases.
     */
    fun unbindCamera() {
        cameraProvider?.unbindAll()
    }
}
