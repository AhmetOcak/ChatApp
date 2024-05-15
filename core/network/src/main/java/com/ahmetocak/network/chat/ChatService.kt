package com.ahmetocak.network.chat

import com.ahmetocak.network.model.NetworkMessage

interface ChatService {

    fun sendMessage(networkMessage: NetworkMessage)
}