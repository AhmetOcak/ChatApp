package com.ahmetocak.network.api.chat

import com.ahmetocak.common.websocket.WebSocketManager
import com.ahmetocak.network.model.NetworkMessage
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ChatServiceImpl @Inject constructor() : ChatService {

    override fun sendMessage(networkMessage: NetworkMessage) {
        val decodedMessage = Json.encodeToString(NetworkMessage.serializer(), networkMessage)
        WebSocketManager.getWebsocketInstance().send(decodedMessage)
    }
}