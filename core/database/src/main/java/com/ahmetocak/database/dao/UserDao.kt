package com.ahmetocak.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ahmetocak.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userEntity: UserEntity)

    @Query("SELECT * FROM UserEntity")
    fun observeUser(): Flow<UserEntity?>

    @Delete(UserEntity::class)
    suspend fun deleteUser(userEntity: UserEntity)

    @Update(UserEntity::class)
    suspend fun updateUser(userEntity: UserEntity)

    @Query("SELECT email FROM UserEntity")
    suspend fun getUserEmail(): String
}