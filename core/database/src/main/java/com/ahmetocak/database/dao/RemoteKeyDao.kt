package com.ahmetocak.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmetocak.database.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKeyEntity: RemoteKeyEntity)

    @Query("SELECT * FROM RemoteKeyEntity WHERE friendship_id == :friendshipId")
    suspend fun getRemoteKey(friendshipId: Int): RemoteKeyEntity?

    @Query("DELETE FROM RemoteKeyEntity WHERE friendship_id == :friendshipId")
    suspend fun clearKey(friendshipId: Int)
}