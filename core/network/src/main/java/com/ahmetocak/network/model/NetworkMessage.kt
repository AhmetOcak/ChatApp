package com.ahmetocak.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedNetworkMessage(
    val messageList: List<NetworkMessage>,
    val totalPages: Long
)

@Serializable
data class NetworkMessage(
    val id: Int = 0,
    val senderEmail: String,
    val receiverEmail: String,
    val messageContent: String,
    val sentAt: String = "",
    val senderImgUrl: String?,
    val senderUsername: String,
    val messageType: String
)