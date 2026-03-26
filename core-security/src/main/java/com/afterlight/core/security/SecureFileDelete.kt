package com.afterlight.core.security

import java.io.File
import java.io.RandomAccessFile
import java.security.SecureRandom

/**
 * DoD 5220.22-M compliant secure file deletion.
 * 3-pass overwrite: 0x00, 0xFF, random bytes.
 */
fun File.secureDelete() {
    if (!exists()) {
        throw IllegalStateException("File does not exist: $absolutePath")
    }
    
    if (!canWrite()) {
        throw SecurityException("Cannot write to file: $absolutePath")
    }
    
    val fileSize = length()
    if (fileSize == 0L) {
        // Empty file, just delete
        delete()
        return
    }
    
    RandomAccessFile(this, "rws").use { raf ->
        // Pass 1: Overwrite with 0x00
        raf.seek(0)
        val zeroBuffer = ByteArray(8192) { 0x00 }
        var remaining = fileSize
        while (remaining > 0) {
            val toWrite = minOf(remaining, zeroBuffer.size.toLong()).toInt()
            raf.write(zeroBuffer, 0, toWrite)
            remaining -= toWrite
        }
        raf.fd.sync() // Force write to disk
        
        // Pass 2: Overwrite with 0xFF
        raf.seek(0)
        val ffBuffer = ByteArray(8192) { 0xFF.toByte() }
        remaining = fileSize
        while (remaining > 0) {
            val toWrite = minOf(remaining, ffBuffer.size.toLong()).toInt()
            raf.write(ffBuffer, 0, toWrite)
            remaining -= toWrite
        }
        raf.fd.sync()
        
        // Pass 3: Overwrite with random bytes
        raf.seek(0)
        val randomBuffer = ByteArray(8192)
        val secureRandom = SecureRandom()
        remaining = fileSize
        while (remaining > 0) {
            val toWrite = minOf(remaining, randomBuffer.size.toLong()).toInt()
            secureRandom.nextBytes(randomBuffer)
            raf.write(randomBuffer, 0, toWrite)
            remaining -= toWrite
        }
        raf.fd.sync()
        
        // Wipe buffers
        zeroBuffer.fill(0)
        ffBuffer.fill(0)
        randomBuffer.fill(0)
    }
    
    // Delete the file
    if (!delete()) {
        throw SecurityException("Failed to delete file after secure wipe: $absolutePath")
    }
}
