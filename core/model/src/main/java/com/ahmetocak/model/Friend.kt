package com.ahmetocak.model

data class Friend(
    val id: Int,
    val currentUserEmail: String,
    val friendEmail: String,
    val friendProfilePicUrl: String?
)
