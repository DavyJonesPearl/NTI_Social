package com.afterlight.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afterlight.feature.auth.domain.AuthRepository
import com.afterlight.feature.auth.domain.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Authentication ViewModel.
 * Stage 13: Login, register with validation and error handling.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    val authState: StateFlow<AuthState> = authRepository.authState as StateFlow<AuthState>
    
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    /**
     * Authenticates user with email and password.
     */
    fun login(email: String, password: String) {
        if (!validateLoginInput(email, password)) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            
            authRepository.login(email, password)
                .onSuccess {
                    _uiState.value = AuthUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState.Error(
                        error.message ?: "Login failed"
                    )
                }
        }
    }
    
    /**
     * Registers new user with age gate validation.
     */
    fun register(
        email: String,
        password: String,
        confirmPassword: String,
        displayName: String?,
        dateOfBirth: Long? // Epoch millis
    ) {
        if (!validateRegisterInput(email, password, confirmPassword, dateOfBirth)) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            
            authRepository.register(email, password, displayName)
                .onSuccess {
                    _uiState.value = AuthUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = AuthUiState.Error(
                        error.message ?: "Registration failed"
                    )
                }
        }
    }
    
    /**
     * Logs out current user and clears all data.
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
    
    /**
     * Clears current UI state (after showing error).
     */
    fun clearUiState() {
        _uiState.value = AuthUiState.Idle
    }
    
    private fun validateLoginInput(email: String, password: String): Boolean {
        if (email.isBlank()) {
            _uiState.value = AuthUiState.Error("Email is required")
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = AuthUiState.Error("Invalid email format")
            return false
        }
        
        if (password.isBlank()) {
            _uiState.value = AuthUiState.Error("Password is required")
            return false
        }
        
        return true
    }
    
    private fun validateRegisterInput(
        email: String,
        password: String,
        confirmPassword: String,
        dateOfBirth: Long?
    ): Boolean {
        if (email.isBlank()) {
            _uiState.value = AuthUiState.Error("Email is required")
            return false
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = AuthUiState.Error("Invalid email format")
            return false
        }
        
        if (password.length < 8) {
            _uiState.value = AuthUiState.Error("Password must be at least 8 characters")
            return false
        }
        
        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Passwords do not match")
            return false
        }
        
        // Age gate: Must be 13+ years old
        if (dateOfBirth == null) {
            _uiState.value = AuthUiState.Error("Date of birth is required")
            return false
        }
        
        val ageMillis = System.currentTimeMillis() - dateOfBirth
        val ageYears = ageMillis / (1000L * 60 * 60 * 24 * 365.25)
        if (ageYears < 13) {
            _uiState.value = AuthUiState.Error("You must be at least 13 years old")
            return false
        }
        
        return true
    }
}

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
