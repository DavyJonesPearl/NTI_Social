package com.afterlight.core.security

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Screen protection manager for FLAG_SECURE enforcement.
 * Stage 13: Prevents screenshots and screen recording.
 */
object ScreenProtectionManager {
    
    /**
     * Enables FLAG_SECURE on the activity window.
     * Blocks screenshots and screen recording.
     */
    fun enableScreenProtection(activity: Activity) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
    
    /**
     * Disables FLAG_SECURE on the activity window.
     */
    fun disableScreenProtection(activity: Activity) {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }
    
    /**
     * Composable that automatically enables FLAG_SECURE for the current activity.
     * Clears flag when composable leaves composition.
     */
    @Composable
    fun ProtectScreen() {
        val context = LocalContext.current
        DisposableEffect(Unit) {
            val activity = context as? Activity
            activity?.let { enableScreenProtection(it) }
            
            onDispose {
                activity?.let { disableScreenProtection(it) }
            }
        }
    }
}
