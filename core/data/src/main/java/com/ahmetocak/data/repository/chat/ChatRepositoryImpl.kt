package com.ahmetocak.data.repository.chat

import com.ahmetocak.data.mapper.toNetworkMessage
import com.ahmetocak.model.Message
import com.ahmetocak.network.api.chat.ChatService
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService
): ChatRepository {

    override fun sendMessage(message: Message, receiverId: String) {
        chatService.sendMessage(message.toNetworkMessage(receiverId))
    }
}