package com.afterlight.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.afterlight.data.local.model.FaceEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for face detection operations.
 * Stage 13: Media-scoped face queries.
 */
@Dao
interface FaceDao {
    
    @Query("SELECT * FROM faces WHERE mediaId = :mediaId")
    fun getFacesForMedia(mediaId: String): Flow<List<FaceEntity>>
    
    @Query("SELECT * FROM faces WHERE mediaId = :mediaId AND isBlurred = 0")
    fun getUnblurredFacesForMedia(mediaId: String): Flow<List<FaceEntity>>
    
    @Query("SELECT COUNT(*) FROM faces WHERE mediaId = :mediaId")
    fun getFaceCountForMedia(mediaId: String): Flow<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(face: FaceEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(faces: List<FaceEntity>)
    
    @Query("UPDATE faces SET isBlurred = :isBlurred WHERE id = :faceId")
    suspend fun updateBlurred(faceId: Long, isBlurred: Boolean)
    
    @Query("DELETE FROM faces WHERE id = :faceId")
    suspend fun deleteById(faceId: Long)
    
    @Query("DELETE FROM faces WHERE mediaId = :mediaId")
    suspend fun deleteByMediaId(mediaId: String)
    
    @Query("DELETE FROM faces")
    suspend fun deleteAll()
}
