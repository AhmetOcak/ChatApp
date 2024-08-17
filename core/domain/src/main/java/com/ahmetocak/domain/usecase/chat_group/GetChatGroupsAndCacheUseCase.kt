package com.ahmetocak.domain.usecase.chat_group

import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.data.repository.chat_group.ChatGroupRepository
import javax.inject.Inject

class GetChatGroupsAndCacheUseCase @Inject constructor(private val repository: ChatGroupRepository) {

    suspend operator fun invoke(userEmail: String, onComplete: suspend (UiText?) -> Unit) {
        when (val response = repository.getGroups(userEmail)) {
            is Response.Success -> onComplete(null)
            is Response.Error -> onComplete(response.errorMessage)
        }
    }
}