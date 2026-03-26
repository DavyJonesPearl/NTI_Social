package com.afterlight.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 * Firebase configuration for debug builds.
 * Configures Firebase Emulators for local development.
 */
object FirebaseDebugConfig {
    
    private const val USE_EMULATORS = false // Set to true when running emulators
    private const val EMULATOR_HOST = "10.0.2.2" // Android emulator localhost alias
    
    // Emulator ports (from firebase.json)
    private const val AUTH_PORT = 9099
    private const val FIRESTORE_PORT = 8080
    
    /**
     * Configure Firebase services for debug mode.
     * Call this in Application.onCreate() when BuildConfig.DEBUG is true.
     */
    fun configure() {
        if (USE_EMULATORS) {
            configureEmulators()
        } else {
            configureProduction()
        }
    }
    
    /**
     * Configure Firebase to use local emulators.
     * 
     * Prerequisites:
     * 1. Install Firebase CLI: npm install -g firebase-tools
     * 2. Start emulators: firebase emulators:start
     * 3. Set USE_EMULATORS = true above
     */
    private fun configureEmulators() {
        try {
            // Configure Authentication Emulator
            FirebaseAuth.getInstance().useEmulator(EMULATOR_HOST, AUTH_PORT)
            
            // Configure Firestore Emulator
            FirebaseFirestore.getInstance().useEmulator(EMULATOR_HOST, FIRESTORE_PORT)
            
            println("🔧 Firebase Emulators configured:")
            println("   Auth: http://$EMULATOR_HOST:$AUTH_PORT")
            println("   Firestore: http://$EMULATOR_HOST:$FIRESTORE_PORT")
            println("   UI: http://localhost:4000")
        } catch (e: Exception) {
            println("⚠️ Failed to configure Firebase Emulators: ${e.message}")
            println("   Make sure emulators are running: firebase emulators:start")
        }
    }
    
    /**
     * Configure Firebase for production (real backend).
     * Enables offline persistence for Firestore.
     */
    private fun configureProduction() {
        try {
            // Disable App Check for development (optional security feature)
            // To enable: Add App Check SDK and register provider
            
            // Enable Firestore offline persistence
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            FirebaseFirestore.getInstance().firestoreSettings = settings
            
            println("🚀 Firebase configured for production (afterlight-d8729)")
            println("   ⚠️ App Check disabled (development mode)")
            println("   ⚠️ reCAPTCHA enforcement disabled (development mode)")
        } catch (e: Exception) {
            println("⚠️ Failed to configure Firebase: ${e.message}")
        }
    }
}
