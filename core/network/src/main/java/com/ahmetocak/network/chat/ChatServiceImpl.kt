package com.ahmetocak.network.chat

import com.ahmetocak.network.model.NetworkMessage
import kotlinx.serialization.json.Json
import okhttp3.WebSocket
import javax.inject.Inject

class ChatServiceImpl @Inject constructor(
    private val webSocket: WebSocket
) : ChatService {

    override fun sendMessage(networkMessage: NetworkMessage) {
        val decodedMessage = Json.encodeToString(NetworkMessage.serializer(), networkMessage)
        webSocket.send(decodedMessage)
    }
}