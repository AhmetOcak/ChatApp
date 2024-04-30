package com.ahmetocak.model

data class UserChatList(
    val chatList: List<UserChat>
)

data class UserChat(
    val id: String,
    val imageUrl: String?,
    val title: String,
    val isSilent: Boolean,
    val lastMessage: String,
    val lastMessageTime: String
)