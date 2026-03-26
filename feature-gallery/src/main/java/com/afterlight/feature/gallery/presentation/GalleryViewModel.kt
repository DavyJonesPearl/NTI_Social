package com.afterlight.feature.gallery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afterlight.data.local.model.MediaEntity
import com.afterlight.feature.gallery.domain.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Gallery ViewModel with reactive media list.
 * Stage 13: NO storage of decrypted bytes, only metadata StateFlow.
 */
@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: GalleryRepository
) : ViewModel() {
    
    private val _mediaList = MutableStateFlow<List<MediaEntity>>(emptyList())
    val mediaList: StateFlow<List<MediaEntity>> = _mediaList
    
    private val _selectedMediaIndex = MutableStateFlow<Int?>(null)
    val selectedMediaIndex: StateFlow<Int?> = _selectedMediaIndex
    
    /**
     * Initializes gallery for a specific party.
     */
    fun loadParty(partyId: String) {
        viewModelScope.launch {
            repository.getMediaForParty(partyId).collect { mediaEntities ->
                _mediaList.value = mediaEntities
            }
        }
    }
    
    /**
     * Handles media selection for full-screen viewing.
     */
    fun onMediaSelected(index: Int) {
        _selectedMediaIndex.value = index
    }
    
    /**
     * Clears media selection (closes full-screen viewer).
     */
    fun clearSelection() {
        _selectedMediaIndex.value = null
    }
}
