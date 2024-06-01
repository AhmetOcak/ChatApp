package com.ahmetocak.domain.usecase.chat

import com.ahmetocak.data.repository.chat.ChatRepository
import com.ahmetocak.model.Message
import javax.inject.Inject

class AddMessageUseCase @Inject constructor(private val chatRepository: ChatRepository) {

    suspend operator fun invoke(message: Message) = chatRepository.addMessage(message)
}