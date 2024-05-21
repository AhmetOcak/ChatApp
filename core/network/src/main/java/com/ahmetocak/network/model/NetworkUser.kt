package com.ahmetocak.network.model

data class NetworkUser(
    val id: Int,
    val username: String,
    val email: String,
    val profilePicUrl: String?
)
