package com.ahmetocak.domain.usecase.chat

import com.ahmetocak.common.MessageManager
import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.common.websocket.WebSocketListener
import com.ahmetocak.data.repository.chat.ChatRepository
import com.ahmetocak.model.Message
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(message: Message, onFailure: (UiText) -> Unit) {
        WebSocketListener.isConnected.collect {
            if (it) {
                chatRepository.sendMessageWithWebSocket(message)
            } else {
                when (val response = chatRepository.sendMessageWithoutWebSocket(message)) {
                    is Response.Success -> {
                        val decodedMessage = Json.encodeToString(Message.serializer(), message)
                        MessageManager.emitMessage(decodedMessage)
                    }
                    is Response.Error -> onFailure(response.errorMessage)
                }
            }
        }
    }
}