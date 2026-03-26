package com.afterlight.core.security

import java.io.File

/**
 * Core security interface for Afterlight encryption operations.
 * Stage 13: AES-256-GCM with per-party key isolation.
 */
interface SecurityManager {
    
    /**
     * Encrypts a file using AES-256-GCM with party-specific key.
     * IV is prepended to the output file (12 bytes).
     * 
     * @param inputFile Plaintext file to encrypt
     * @param outputFile Encrypted file destination
     * @param partyId Party identifier for key isolation
     * @throws SecurityException if encryption fails
     */
    suspend fun encryptFile(inputFile: File, outputFile: File, partyId: String)
    
    /**
     * Decrypts a file encrypted with encryptFile().
     * Reads IV from first 12 bytes of input file.
     * 
     * @param inputFile Encrypted file with prepended IV
     * @param outputFile Decrypted file destination
     * @param partyId Party identifier for key isolation
     * @throws SecurityException if decryption fails or GCM tag invalid
     */
    suspend fun decryptFile(inputFile: File, outputFile: File, partyId: String)
    
    /**
     * Securely deletes a file using DoD 5220.22-M 3-pass wipe.
     * Pass 1: 0x00, Pass 2: 0xFF, Pass 3: Random bytes.
     * 
     * @param file File to securely delete
     * @return true if deletion succeeded, false otherwise
     */
    suspend fun deleteSecurely(file: File): Boolean
    
    /**
     * Rotates the encryption key for a specific party.
     * All existing encrypted files must be re-encrypted after rotation.
     * 
     * @param partyId Party identifier
     * @throws SecurityException if key rotation fails
     */
    suspend fun rotatePartyKey(partyId: String)
}
