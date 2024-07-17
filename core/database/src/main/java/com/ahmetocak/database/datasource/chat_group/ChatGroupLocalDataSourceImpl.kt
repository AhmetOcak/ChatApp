package com.ahmetocak.database.datasource.chat_group

import com.ahmetocak.database.dao.ChatGroupDao
import com.ahmetocak.database.entity.ChatGroupEntity
import com.ahmetocak.database.entity.ChatGroupParticipantsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatGroupLocalDataSourceImpl @Inject constructor(
    private val chatGroupDao: ChatGroupDao
) : ChatGroupLocalDataSource {
    override suspend fun insertChatGroup(chatGroupEntity: ChatGroupEntity) {
        return chatGroupDao.insertChatGroup(chatGroupEntity)
    }

    override suspend fun insertChatGroupParticipants(chatGroupParticipantsEntity: ChatGroupParticipantsEntity) {
        return chatGroupDao.insertChatGroupParticipants(chatGroupParticipantsEntity)
    }

    override suspend fun getAllChatGroups(): Flow<List<ChatGroupEntity>> {
        return chatGroupDao.getAllChatGroups()
    }

    override suspend fun getChatGroupParticipants(): Flow<List<ChatGroupParticipantsEntity>> {
        return chatGroupDao.getChatGroupParticipants()
    }
}