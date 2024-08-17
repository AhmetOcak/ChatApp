package com.ahmetocak.network.model

data class NetworkChatGroup(
    val id: Int = 0,
    val name: String,
    val imageUrl: String?,
    val participants: List<NetworkChatGroupParticipants>,
    val groupType: String
)

data class NetworkChatGroupParticipants(
    val id: Int = 0,
    val participantEmail: String,
    val participantUsername: String,
    val participantProfilePicUrl: String?
)
