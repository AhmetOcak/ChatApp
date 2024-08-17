package com.ahmetocak.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatGroup(
    val id: Int = 0,
    val name: String,
    val imageUrl: String?,
    val participants: List<ChatGroupParticipants>,
    val groupType: GroupType
)

@Serializable
data class ChatGroupParticipants(
    val groupId: Int = 0,
    val participantEmail: String,
    val participantUsername: String,
    val participantProfilePicUrl: String?
)

enum class GroupType {
    PRIVATE_CHAT_GROUP,
    CHAT_GROUP
}
