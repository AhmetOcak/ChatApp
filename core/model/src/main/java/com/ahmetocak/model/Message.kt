package com.ahmetocak.model

data class Message(
    val authorId: String,
    val author: String,
    val authorImage: String,
    val message: String,
    val time: String
) {
    fun isComingFromMe(authorId: String) = this.authorId == authorId
}
