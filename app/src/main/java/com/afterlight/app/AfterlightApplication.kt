package com.afterlight.app

import android.app.Application
import android.os.StrictMode
import android.os.SystemClock
import android.util.Log
import com.afterlight.core.security.PlaintextLeakDetector
import dagger.hilt.android.HiltAndroidApp
import net.zetetic.database.sqlcipher.SQLiteDatabase

/**
 * Stage 12A: Production Hardening - StrictMode Enforcement
 * Stage 14A: Performance - Cold Start Benchmark
 * 
 * Initialization:
 * - Hilt dependency injection
 * - Database initialization (via Hilt)
 * - SecurityManager initialization (via Hilt)
 * - StrictMode (debug builds only)
 * - Plaintext leak detector (debug builds only)
 * - Cold start timing (debug builds only)
 * 
 * STRICTMODE POLICY (DEBUG ONLY):
 * • Detect disk reads/writes on main thread
 * • Detect network operations on main thread
 * • Detect leaked closable objects
 * • Penalty: Log only (no crash)
 * 
 * COLD START BENCHMARK (DEBUG ONLY):
 * • Measure Application.onCreate() time
 * • Measure time to first composable draw
 * • Target: < 2000ms (release build)
 * 
 * PURPOSE:
 * • Catch main thread I/O violations early
 * • Validate camera/gallery use IO dispatcher
 * • Prevent ANRs in production
 * • Profile cold start performance
 */
@HiltAndroidApp
class AfterlightApplication : Application() {
    
    companion object {
        private const val TAG = "AfterlightApp"
        
        // Cold start timing (Stage 14A)
        private var appStartTime: Long = 0
        
        /**
         * Record app start time (called from MainActivity.onCreate).
         * 
         * TIMING:
         * appCreateTime = Application.onCreate completion
         * appStartTime = First composable drawn (setContent called)
         * 
         * Cold start = appStartTime - process start time
         */
        fun recordAppStart() {
            appStartTime = SystemClock.elapsedRealtime()
        }
    }
    
    private var appCreateTime: Long = 0
    
    override fun onCreate() {
        val processStartTime = SystemClock.elapsedRealtime()
        super.onCreate()
        
        // Initialize SQLCipher native libraries
        System.loadLibrary("sqlcipher")
        
        // Hilt handles all dependency injection
        // Database and SecurityManager initialized on-demand
        
        // Enable debug tools
        if (BuildConfig.DEBUG) {
            enableStrictMode()
            scanForPlaintextLeaks()
            FirebaseDebugConfig.configure() // Configure Firebase emulators/production
        }
        
        appCreateTime = SystemClock.elapsedRealtime()
        
        // Log cold start timing (debug only)
        if (BuildConfig.DEBUG) {
            val initTime = appCreateTime - processStartTime
            Log.i(TAG, "📊 Application.onCreate() completed in ${initTime}ms")
        }
    }
    
    /**
     * Enable StrictMode for development.
     * 
     * THREAD POLICY:
     * • detectDiskReads() - Catch file reads on main thread
     * • detectDiskWrites() - Catch file writes on main thread
     * • detectNetwork() - Catch network calls on main thread
     * 
     * VM POLICY:
     * • detectLeakedSqlLiteObjects() - Catch unclosed database cursors
     * • detectLeakedClosableObjects() - Catch unclosed streams (API 30+)
     * 
     * PENALTY:
     * • penaltyLog() - Write to logcat (visible in Android Studio)
     * • NOT penaltyDeath() - Don't crash (allows testing)
     * 
     * EXPECTED VIOLATIONS: ZERO
     * • Camera encryption: Dispatchers.IO ✓
     * • Gallery decryption: withContext(Dispatchers.IO) ✓
     * • Database queries: Room handles threading ✓
     * • Network calls: Retrofit handles threading ✓
     */
    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )
        
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .apply {
                    // detectLeakedClosableObjects() requires API 30+
                    if (android.os.Build.VERSION.SDK_INT >= 30) {
                        detectLeakedClosableObjects()
                    }
                }
                .penaltyLog()
                .build()
        )
    }
    
    /**
     * Scan app storage for plaintext media files (Stage 12B).
     * 
     * SCANS:
     * • filesDir - Should only contain .enc files in /parties/
     * • cacheDir - Should be empty or minimal
     * • externalFilesDir - Should not contain media
     * 
     * LOGS WARNINGS IF FOUND:
     * • .jpg, .png, .mp4 files anywhere
     * • Non-.enc files in /parties/ directory
     * 
     * Does NOT crash app (allows debugging).
     */
    private fun scanForPlaintextLeaks() {
        PlaintextLeakDetector.scanForPlaintextFiles(this)
    }
}
