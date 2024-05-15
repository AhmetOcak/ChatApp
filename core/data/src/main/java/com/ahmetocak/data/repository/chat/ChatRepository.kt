package com.ahmetocak.data.repository.chat

import com.ahmetocak.model.Message

interface ChatRepository {

    fun sendMessage(message: Message, receiverId: String)
}