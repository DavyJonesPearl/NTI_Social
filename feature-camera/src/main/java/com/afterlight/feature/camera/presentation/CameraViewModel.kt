package com.afterlight.feature.camera.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afterlight.data.local.model.MediaEntity
import com.afterlight.feature.camera.domain.CameraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * Camera ViewModel with capture state management.
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repository: CameraRepository
) : ViewModel() {
    
    private val _captureState = MutableStateFlow<CaptureState>(CaptureState.Idle)
    val captureState: StateFlow<CaptureState> = _captureState
    
    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted
    
    fun capturePhoto(partyId: String, jpegFile: File) {
        viewModelScope.launch {
            _captureState.value = CaptureState.Capturing
            repository.capturePhoto(partyId, jpegFile).fold(
                onSuccess = { mediaEntity ->
                    _captureState.value = CaptureState.Success(mediaEntity)
                },
                onFailure = { error ->
                    _captureState.value = CaptureState.Error(error.message ?: "Capture failed")
                }
            )
        }
    }
    
    fun updatePermission(granted: Boolean) {
        _permissionGranted.value = granted
    }
    
    fun resetCaptureState() {
        _captureState.value = CaptureState.Idle
    }
}

sealed class CaptureState {
    data object Idle : CaptureState()
    data object Capturing : CaptureState()
    data class Success(val mediaEntity: MediaEntity) : CaptureState()
    data class Error(val message: String) : CaptureState()
}
