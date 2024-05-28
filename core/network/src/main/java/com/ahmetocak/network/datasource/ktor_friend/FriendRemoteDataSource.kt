package com.ahmetocak.network.datasource.ktor_friend

import com.ahmetocak.common.Response
import com.ahmetocak.network.model.NetworkFriend

interface FriendRemoteDataSource {
    suspend fun getFriends(userEmail: String): Response<List<NetworkFriend>>
    suspend fun createFriend(userEmail: String, friendEmail: String): Response<NetworkFriend>
}