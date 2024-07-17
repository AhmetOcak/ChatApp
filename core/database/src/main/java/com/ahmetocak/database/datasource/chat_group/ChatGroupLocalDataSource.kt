package com.ahmetocak.database.datasource.chat_group

import com.ahmetocak.database.entity.ChatGroupEntity
import com.ahmetocak.database.entity.ChatGroupParticipantsEntity
import kotlinx.coroutines.flow.Flow

interface ChatGroupLocalDataSource {
    suspend fun insertChatGroup(chatGroupEntity: ChatGroupEntity)
    suspend fun insertChatGroupParticipants(chatGroupParticipantsEntity: ChatGroupParticipantsEntity)
    suspend fun getAllChatGroups(): Flow<List<ChatGroupEntity>>
    suspend fun getChatGroupParticipants(): Flow<List<ChatGroupParticipantsEntity>>
}