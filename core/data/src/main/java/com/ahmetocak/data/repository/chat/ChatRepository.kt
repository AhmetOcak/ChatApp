package com.ahmetocak.data.repository.chat

import androidx.paging.PagingData
import com.ahmetocak.common.Response
import com.ahmetocak.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun sendMessage(message: Message)

    fun getMessages(userEmail: String, friendEmail: String): Flow<PagingData<Message>>

    suspend fun addMessage(message: Message): Response<Unit>
}