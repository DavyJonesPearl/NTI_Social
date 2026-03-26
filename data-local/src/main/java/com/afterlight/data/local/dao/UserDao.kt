package com.afterlight.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.afterlight.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for user operations.
 * Stage 13: Reactive queries with Flow.
 */
@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): Flow<UserEntity?>
    
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)
    
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteById(userId: String)
    
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}
