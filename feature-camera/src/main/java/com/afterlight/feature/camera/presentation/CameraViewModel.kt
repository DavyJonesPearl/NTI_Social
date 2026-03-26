package com.afterlight.feature.camera.presentation

import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afterlight.data.local.model.MediaEntity
import com.afterlight.feature.camera.domain.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Camera ViewModel with capture state management.
 * Stage 13: Handles photo capture, encryption, and permission state.
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: CameraRepository
) : ViewModel() {
    
    private val _captureState = MutableStateFlow<CaptureState>(CaptureState.Idle)
    val captureState: StateFlow<CaptureState> = _captureState
    
    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted
    
    /**
     * Captures photo from ImageProxy and processes it.
     */
    fun capturePhoto(partyId: String, imageProxy: ImageProxy) {
        viewModelScope.launch {
            _captureState.value = CaptureState.Capturing
            
            repository.capturePhoto(partyId, imageProxy).fold(
                onSuccess = { mediaEntity ->
                    _captureState.value = CaptureState.Success(mediaEntity)
                },
                onFailure = { error ->
                    _captureState.value = CaptureState.Error(error.message ?: "Capture failed")
                }
            )
            
            // Close ImageProxy after processing
            imageProxy.close()
        }
    }
    
    /**
     * Updates camera permission state.
     */
    fun updatePermission(granted: Boolean) {
        _permissionGranted.value = granted
    }
    
    /**
     * Resets capture state to idle.
     */
    fun resetCaptureState() {
        _captureState.value = CaptureState.Idle
    }
}

/**
 * Camera capture state sealed class.
 */
sealed class CaptureState {
    data object Idle : CaptureState()
    data object Capturing : CaptureState()
    data class Success(val mediaEntity: MediaEntity) : CaptureState()
    data class Error(val message: String) : CaptureState()
}
