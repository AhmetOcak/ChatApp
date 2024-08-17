package com.ahmetocak.domain.usecase.chat_group

import com.ahmetocak.data.repository.chat_group.ChatGroupRepository
import javax.inject.Inject

class ObserveChatGroupsInCache @Inject constructor(private val repository: ChatGroupRepository) {
    suspend operator fun invoke() = repository.observeGroups()
}