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

    @Query("SELECT * FROM RemoteKeyEntity")
    suspend fun getRemoteKey(): RemoteKeyEntity?

    @Query("DELETE FROM RemoteKeyEntity")
    suspend fun clearKey()
}