package com.ahmetocak.domain.usecase.chat

import com.ahmetocak.data.repository.chat.ChatRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val chatRepository: ChatRepository) {

    operator fun invoke(userEmail: String, friendEmail: String) =
        chatRepository.getMessages(userEmail = userEmail, friendEmail = friendEmail)
}