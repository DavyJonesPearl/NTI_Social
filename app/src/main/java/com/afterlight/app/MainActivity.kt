package com.afterlight.app

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

/**
 * Stage 13A: Root Navigation Integration
 * Stage 14A: Performance - Cold Start Benchmark
 * 
 * MainActivity - Entry point with full navigation.
 * 
 * RESPONSIBILITIES:
 * • Initialize Hilt dependency injection
 * • Set content to AfterlightApp (root navigation orchestrator)
 * • Handle system UI (status bar, navigation bar)
 * • Measure cold start time (debug builds)
 * 
 * NAVIGATION:
 * • AfterlightApp manages authentication gate
 * • Unauthenticated → auth_graph (login/register)
 * • Authenticated → party_graph (party list/detail)
 * 
 * COLD START TIMING (Stage 14A):
 * • Application.onCreate() → MainActivity.onCreate() → setContent()
 * • Target: < 2000ms (release build)
 * • Measured via SystemClock.elapsedRealtime()
 * 
 * LIFECYCLE:
 * • onCreate: Initialize app + measure timing
 * • Hilt injects dependencies on-demand
 * • AfterlightApp survives configuration changes
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val activityStartTime = SystemClock.elapsedRealtime()
        super.onCreate(savedInstanceState)
        
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AfterlightApp()
                }
            }
        }
        
        // Record app start time (debug only)
        if (BuildConfig.DEBUG) {
            AfterlightApplication.recordAppStart()
            val contentSetTime = SystemClock.elapsedRealtime()
            val activityTime = contentSetTime - activityStartTime
            Log.i(TAG, "📊 MainActivity.setContent() completed in ${activityTime}ms")
            
            // Note: Full cold start time includes:
            // - Process initialization
            // - Application.onCreate()
            // - MainActivity.onCreate()
            // - First composable draw
            // This will be measured via Android Studio Profiler or logcat
        }
    }
}
