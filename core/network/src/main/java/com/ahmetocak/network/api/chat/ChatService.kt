package com.ahmetocak.network.api.chat

import com.ahmetocak.network.model.NetworkMessage

interface ChatService {

    fun sendMessage(networkMessage: NetworkMessage)
}