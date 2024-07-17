package com.ahmetocak.domain.usecase.chat_group

import com.ahmetocak.data.repository.chat_group.ChatGroupRepository
import javax.inject.Inject

class AddParticipantToGroupUseCase @Inject constructor(private val repository: ChatGroupRepository) {

    suspend operator fun invoke(
        groupId: Int,
        participantEmail: String,
        participantUsername: String,
        participantProfilePicUrl: String?
    ) = repository.addParticipantToChatGroup(
        groupId = groupId,
        participantEmail = participantEmail,
        participantUsername = participantUsername,
        participantProfilePicUrl = participantProfilePicUrl
    )
}