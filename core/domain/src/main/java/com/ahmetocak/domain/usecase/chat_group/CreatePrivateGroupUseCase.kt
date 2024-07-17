package com.ahmetocak.domain.usecase.chat_group

import com.ahmetocak.data.repository.chat_group.ChatGroupRepository
import javax.inject.Inject

class CreatePrivateGroupUseCase @Inject constructor(private val repository: ChatGroupRepository) {
    suspend operator fun invoke(
        currentUserEmail: String,
        friendEmail: String
    ) = repository.createPrivateGroup(currentUserEmail, friendEmail)
}