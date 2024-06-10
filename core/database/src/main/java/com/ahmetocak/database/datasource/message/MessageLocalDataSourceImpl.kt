package com.ahmetocak.database.datasource.message

import com.ahmetocak.common.Response
import com.ahmetocak.database.dao.MessageDao
import com.ahmetocak.database.entity.MessageEntity
import com.ahmetocak.database.utils.dbCall
import javax.inject.Inject

class MessageLocalDataSourceImpl @Inject constructor(
    private val messageDao: MessageDao
): MessageLocalDataSource {
    override suspend fun addMessage(messageEntity: MessageEntity): Response<Unit> {
        return dbCall { messageDao.addMessage(messageEntity) }
    }
}