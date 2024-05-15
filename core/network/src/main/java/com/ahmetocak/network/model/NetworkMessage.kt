package com.ahmetocak.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkMessage(
    val senderId: String,
    val senderName: String,
    val senderImage: String?,
    val receiverId: String,
    val content: String,
    val time: String
)