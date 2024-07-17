package com.ahmetocak.model

data class ChatGroup(
    val id: Int = 0,
    val name: String,
    val imageUrl: String?,
    val participants: List<ChatGroupParticipants>,
    val groupType: GroupType
)

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
