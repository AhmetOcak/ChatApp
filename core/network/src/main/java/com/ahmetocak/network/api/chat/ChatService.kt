package com.ahmetocak.network.api.chat

import com.ahmetocak.common.Response
import com.ahmetocak.network.model.NetworkMessage

interface ChatService {

    fun sendMessageWithWebSocket(networkMessage: NetworkMessage)
    suspend fun sendMessageWithoutWebSocket(networkMessage: NetworkMessage): Response<NetworkMessage>
}