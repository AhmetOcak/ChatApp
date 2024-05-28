package com.ahmetocak.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmetocak.database.entity.FriendEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FriendDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFriend(friendEntity: FriendEntity)

    @Query("SELECT * FROM FriendEntity")
    fun observeFriends(): Flow<List<FriendEntity>>
}