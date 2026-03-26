package com.afterlight.core.security

import android.content.Context
import android.util.Log
import java.io.File

/**
 * Plaintext leak detector for debug builds.
 * Stage 12: Scans app storage for unencrypted sensitive files.
 */
object PlaintextLeakDetector {
    
    private const val TAG = "PlaintextLeakDetector"
    
    /**
     * Scans app files directory for potential plaintext leaks.
     * Checks for .jpg, .png, .mp4 files outside encrypted directories.
     */
    fun scanForPlaintextFiles(context: Context) {
        val filesDir = context.filesDir
        val encryptedDir = File(filesDir, "parties")
        
        val suspiciousFiles = mutableListOf<File>()
        
        filesDir.walkTopDown().forEach { file ->
            if (file.isFile && !file.absolutePath.startsWith(encryptedDir.absolutePath)) {
                val extension = file.extension.lowercase()
                if (extension in listOf("jpg", "jpeg", "png", "mp4", "mov")) {
                    suspiciousFiles.add(file)
                }
            }
        }
        
        if (suspiciousFiles.isNotEmpty()) {
            Log.w(TAG, "⚠️ Found ${suspiciousFiles.size} potential plaintext media files:")
            suspiciousFiles.forEach { file ->
                Log.w(TAG, "  - ${file.absolutePath} (${file.length()} bytes)")
            }
        } else {
            Log.d(TAG, "✅ No plaintext media files detected")
        }
    }
}
