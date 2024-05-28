package com.ahmetocak.network.datasource.ktor_friend

import com.ahmetocak.common.Response
import com.ahmetocak.network.api.KtorChatApi
import com.ahmetocak.network.helper.apiCall
import com.ahmetocak.network.model.NetworkFriend
import javax.inject.Inject

class FriendRemoteDataSourceImpl @Inject constructor(private val api: KtorChatApi): FriendRemoteDataSource {
    override suspend fun getFriends(userEmail: String): Response<List<NetworkFriend>> {
        return apiCall { api.getFriends(userEmail) }
    }

    override suspend fun createFriend(userEmail: String, friendEmail: String): Response<NetworkFriend> {
        return apiCall { api.createFriend(userEmail = userEmail, friendEmail = friendEmail) }
    }
}