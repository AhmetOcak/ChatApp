package com.ahmetocak.data.repository.friend

import com.ahmetocak.common.Response
import com.ahmetocak.model.Friend
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    suspend fun getFriends(userEmail: String): Response<List<Friend>>
    suspend fun createFriend(userEmail: String, friendEmail: String): Response<Friend>
    suspend fun addFriendToCache(friend: Friend): Response<Unit>
    suspend fun observeFriendInCache(): Response<Flow<List<Friend>>>
}