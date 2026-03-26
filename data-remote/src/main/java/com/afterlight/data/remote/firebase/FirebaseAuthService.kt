package com.afterlight.data.remote.firebase

import com.afterlight.data.remote.dto.LoginRequest
import com.afterlight.data.remote.dto.LoginResponse
import com.afterlight.data.remote.dto.RegisterRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Firebase Authentication Service
 * Replaces Retrofit-based AuthApi for registration, login, and logout
 */
@Singleton
class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    
    /**
     * Register new user with email/password
     * @return LoginResponse with mock tokens (Firebase handles auth internally)
     */
    suspend fun register(request: RegisterRequest): Result<LoginResponse> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(
                request.email,
                request.password
            ).await()
            
            val user = authResult.user
            if (user != null) {
                // Get Firebase ID token for API authentication
                val idToken = user.getIdToken(false).await().token ?: ""
                
                Result.success(
                    LoginResponse(
                        accessToken = idToken,
                        refreshToken = idToken, // Firebase manages refresh internally
                        userId = user.uid,
                        email = user.email ?: request.email,
                        displayName = user.displayName
                    )
                )
            } else {
                Result.failure(Exception("Registration failed: User is null"))
            }
        } catch (e: com.google.firebase.FirebaseNetworkException) {
            Result.failure(Exception("Network error: Please check your internet connection", e))
        } catch (e: com.google.firebase.auth.FirebaseAuthException) {
            // Handle Firebase Auth specific errors
            val message = when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already in use"
                "ERROR_WEAK_PASSWORD" -> "Password is too weak"
                "ERROR_INVALID_EMAIL" -> "Invalid email format"
                "CONFIGURATION_NOT_FOUND" -> "Firebase not configured properly (reCAPTCHA/App Check)"
                else -> "Registration failed: ${e.message}"
            }
            Result.failure(Exception(message, e))
        } catch (e: Exception) {
            Result.failure(Exception("Registration failed: ${e.message}", e))
        }
    }
    
    /**
     * Login with email/password
     * @return LoginResponse with Firebase ID token
     */
    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(
                request.email,
                request.password
            ).await()
            
            val user = authResult.user
            if (user != null) {
                val idToken = user.getIdToken(false).await().token ?: ""
                
                Result.success(
                    LoginResponse(
                        accessToken = idToken,
                        refreshToken = idToken,
                        userId = user.uid,
                        email = user.email ?: request.email,
                        displayName = user.displayName
                    )
                )
            } else {
                Result.failure(Exception("Login failed: User is null"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Login failed: ${e.message}", e))
        }
    }
    
    /**
     * Logout current user
     */
    suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Logout failed: ${e.message}", e))
        }
    }
    
    /**
     * Get current Firebase user
     */
    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
    
    /**
     * Refresh Firebase ID token
     */
    suspend fun refreshToken(): Result<String> {
        return try {
            val user = firebaseAuth.currentUser
            if (user != null) {
                val idToken = user.getIdToken(true).await().token ?: ""
                Result.success(idToken)
            } else {
                Result.failure(Exception("No user logged in"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Token refresh failed: ${e.message}", e))
        }
    }
}
