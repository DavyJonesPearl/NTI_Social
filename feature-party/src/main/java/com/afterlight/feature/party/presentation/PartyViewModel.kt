package com.afterlight.feature.party.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afterlight.data.local.model.PartyEntity
import com.afterlight.feature.party.domain.PartyRepository
import com.afterlight.feature.party.worker.PartyExpirationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import javax.inject.Inject

/**
 * Party ViewModel.
 * Stage 13: Party list, create, join, delete operations.
 */
@HiltViewModel
class PartyViewModel @Inject constructor(
    private val partyRepository: PartyRepository,
    private val expirationScheduler: PartyExpirationScheduler
) : ViewModel() {
    
    private val _parties = MutableStateFlow<List<PartyEntity>>(emptyList())
    val parties: StateFlow<List<PartyEntity>> = _parties.asStateFlow()
    
    private val _uiState = MutableStateFlow<PartyUiState>(PartyUiState.Idle)
    val uiState: StateFlow<PartyUiState> = _uiState.asStateFlow()

    private val _selectedParty = MutableStateFlow<PartyEntity?>(null)
    val selectedParty: StateFlow<PartyEntity?> = _selectedParty.asStateFlow()

    private var selectedPartyJob: Job? = null
    
    init {
        loadParties()
    }

    fun loadParty(partyId: String) {
        selectedPartyJob?.cancel()
        selectedPartyJob = viewModelScope.launch {
            partyRepository.getPartyById(partyId).collect { party ->
                _selectedParty.value = party
            }
        }
    }
    
    /**
     * Loads user's active parties.
     */
    private fun loadParties() {
        viewModelScope.launch {
            partyRepository.fetchUserParties().collect { partyList ->
                _parties.value = partyList
            }
        }
    }
    
    /**
     * Creates a new party and schedules expiration.
     */
    fun createParty(name: String, expiresAt: Instant) {
        if (name.isBlank()) {
            _uiState.value = PartyUiState.Error("Party name is required")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = PartyUiState.Loading
            
            partyRepository.createParty(name, expiresAt)
                .onSuccess { party ->
                    // Schedule expiration worker
                    expirationScheduler.scheduleExpiration(party.id, party.expiresAt)
                    _uiState.value = PartyUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = PartyUiState.Error(
                        error.message ?: "Failed to create party"
                    )
                }
        }
    }
    
    /**
     * Joins an existing party.
     */
    fun joinParty(partyId: String) {
        if (partyId.isBlank()) {
            _uiState.value = PartyUiState.Error("Party ID is required")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = PartyUiState.Loading
            
            partyRepository.joinParty(partyId)
                .onSuccess { party ->
                    // Schedule expiration worker
                    expirationScheduler.scheduleExpiration(party.id, party.expiresAt)
                    _uiState.value = PartyUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = PartyUiState.Error(
                        error.message ?: "Failed to join party"
                    )
                }
        }
    }
    
    /**
     * Deletes a party and cancels expiration worker.
     */
    fun deleteParty(partyId: String) {
        viewModelScope.launch {
            _uiState.value = PartyUiState.Loading
            
            partyRepository.deleteParty(partyId)
                .onSuccess {
                    // Cancel expiration worker
                    expirationScheduler.cancelExpiration(partyId)
                    _uiState.value = PartyUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = PartyUiState.Error(
                        error.message ?: "Failed to delete party"
                    )
                }
        }
    }
    
    /**
     * Clears current UI state.
     */
    fun clearUiState() {
        _uiState.value = PartyUiState.Idle
    }
}

sealed class PartyUiState {
    object Idle : PartyUiState()
    object Loading : PartyUiState()
    object Success : PartyUiState()
    data class Error(val message: String) : PartyUiState()
}
