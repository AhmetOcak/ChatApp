package com.ahmetocak.data.repository.chat

import androidx.paging.PagingData
import com.ahmetocak.common.Response
import com.ahmetocak.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun sendMessageWithWebSocket(message: Message)
    suspend fun sendMessageWithoutWebSocket(message: Message): Response<Message>
    fun getMessages(friendshipId: Int): Flow<PagingData<Message>>
    suspend fun addMessage(message: Message): Response<Unit>
    suspend fun getAllMediaMessages(messageBoxId: Int, ): Response<List<Message>>
}