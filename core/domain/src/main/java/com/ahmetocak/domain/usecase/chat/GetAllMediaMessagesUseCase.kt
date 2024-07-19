package com.ahmetocak.domain.usecase.chat

import com.ahmetocak.data.repository.chat.ChatRepository
import javax.inject.Inject

class GetAllMediaMessagesUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(messageBoxId: Int) = repository.getAllMediaMessages(messageBoxId)
}