package com.ahmetocak.domain.usecase.chat

import com.ahmetocak.common.MessageManager
import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.common.websocket.Connection
import com.ahmetocak.common.websocket.WebSocketListener
import com.ahmetocak.data.repository.chat.ChatRepository
import com.ahmetocak.model.Message
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(message: Message, onFailure: (UiText) -> Unit) {
        when (WebSocketListener.isConnected.value) {
            Connection.CONNECTED -> chatRepository.sendMessageWithWebSocket(message)
            Connection.NOT_CONNECTED -> {
                when (val response = chatRepository.sendMessageWithoutWebSocket(message)) {
                    is Response.Success -> {
                        val decodedMessage = Json.encodeToString(Message.serializer(), response.data)
                        MessageManager.emitMessage(decodedMessage)
                    }
                    is Response.Error -> onFailure(response.errorMessage)
                }
            }
        }
    }
}