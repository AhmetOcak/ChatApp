package com.ahmetocak.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.ahmetocak.database.entity.ChatGroupEntity
import com.ahmetocak.database.entity.ChatGroupParticipantsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatGroupDao {

    @Upsert
    suspend fun insertChatGroup(chatGroupEntity: ChatGroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatGroupParticipants(chatGroupParticipantsEntity: ChatGroupParticipantsEntity)

    @Delete
    suspend fun removeParticipantFromGroup(chatGroupParticipantsEntity: ChatGroupParticipantsEntity)

    @Query("SELECT * FROM chatgroupentity")
    fun getAllChatGroups(): Flow<List<ChatGroupEntity>>

    @Query("SELECT * FROM chatgroupparticipantsentity")
    fun getChatGroupParticipants(): Flow<List<ChatGroupParticipantsEntity>>
}