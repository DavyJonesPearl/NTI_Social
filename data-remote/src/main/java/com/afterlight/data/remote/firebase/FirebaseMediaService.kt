package com.afterlight.data.remote.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service to handle media file uploads/downloads with Firebase Storage and metadata syncing in Firestore.
 */
@Singleton
class FirebaseMediaService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {
    
    suspend fun uploadMedia(partyId: String, mediaId: String, encryptedFile: File): Result<Unit> {
        return try {
            val currentUserId = auth.currentUser?.uid 
                ?: return Result.failure(Exception("User not authenticated"))
            
            // 1. Upload file to Firebase Storage under /encrypted-media/{partyId}/{mediaId}
            val storageRef = storage.getReference("encrypted-media/$partyId/$mediaId")
            val uri = Uri.fromFile(encryptedFile)
            storageRef.putFile(uri).await()
            
            // 2. Sync metadata to Firestore under /parties/{partyId}/media/{mediaId}
            val mediaMetadata = hashMapOf(
                "id" to mediaId,
                "partyId" to partyId,
                "userId" to currentUserId,
                "createdAt" to FieldValue.serverTimestamp(),
                "flagged" to false
            )
            firestore.collection("parties").document(partyId)
                .collection("media").document(mediaId)
                .set(mediaMetadata).await()
                
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadMedia(partyId: String, mediaId: String, destinationFile: File): Result<Unit> {
        return try {
            // Download from Firebase Storage
            val storageRef = storage.getReference("encrypted-media/$partyId/$mediaId")
            storageRef.getFile(destinationFile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
