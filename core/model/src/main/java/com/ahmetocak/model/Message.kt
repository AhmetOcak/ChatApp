package com.ahmetocak.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(

    @SerialName("senderId")
    val authorId: String,

    @SerialName("senderName")
    val authorName: String,

    @SerialName("senderImage")
    val authorImage: String?,

    @SerialName("content")
    val message: String,

    @SerialName("time")
    val time: String
) {
    fun isComingFromMe(authorId: String) = this.authorId == authorId
}
