package com.ahmetocak.network.api.chat

import com.ahmetocak.common.Response
import com.ahmetocak.common.websocket.WebSocketManager
import com.ahmetocak.network.api.KtorChatApi
import com.ahmetocak.network.helper.apiCall
import com.ahmetocak.network.model.NetworkMessage
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatServiceImpl @Inject constructor(private val api: KtorChatApi) : ChatService {

    override fun sendMessageWithWebSocket(networkMessage: NetworkMessage) {
        val decodedMessage = Json.encodeToString(NetworkMessage.serializer(), networkMessage)
        WebSocketManager.getWebsocketInstance().send(decodedMessage)
    }

    override suspend fun sendMessageWithoutWebSocket(networkMessage: NetworkMessage): Response<NetworkMessage> {
        return apiCall { api.sendMessage(networkMessage) }
    }
}