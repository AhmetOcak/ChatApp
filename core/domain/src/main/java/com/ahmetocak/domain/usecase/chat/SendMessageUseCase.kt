package com.ahmetocak.domain.usecase.chat

import com.ahmetocak.data.repository.chat.ChatRepository
import com.ahmetocak.model.Message
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val chatRepository: ChatRepository) {

    operator fun invoke(message: Message) = chatRepository.sendMessage(message)
}