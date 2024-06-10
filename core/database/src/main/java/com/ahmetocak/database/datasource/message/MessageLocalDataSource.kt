package com.ahmetocak.database.datasource.message

import com.ahmetocak.common.Response
import com.ahmetocak.database.entity.MessageEntity

interface MessageLocalDataSource {
    suspend fun addMessage(messageEntity: MessageEntity): Response<Unit>
}