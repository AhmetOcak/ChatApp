package com.ahmetocak.network.model

data class NetworkFriend(
    val id: Int,
    val userEmail: String,
    val friendEmail: String,
    val friendProfilePicUrl: String?
)
