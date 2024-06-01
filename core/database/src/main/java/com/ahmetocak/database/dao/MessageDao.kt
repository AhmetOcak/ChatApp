package com.ahmetocak.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmetocak.database.entity.MessageEntity

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(messageEntity: MessageEntity)

    @Query("SELECT * FROM messageentity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, MessageEntity>

    @Query("DELETE FROM messageentity")
    suspend fun clearAll()
}