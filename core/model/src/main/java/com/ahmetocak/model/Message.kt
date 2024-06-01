package com.ahmetocak.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int = 0,
    val senderEmail: String,
    val receiverEmail: String,
    val messageText: String,
    val sentAt: String = "",
    val senderImgUrl: String?,
    val senderUsername: String
) {
    fun isComingFromMe(senderEmail: String) = this.senderEmail == senderEmail
}