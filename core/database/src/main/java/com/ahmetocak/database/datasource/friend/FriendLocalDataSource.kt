package com.ahmetocak.database.datasource.friend

import com.ahmetocak.common.Response
import com.ahmetocak.database.entity.FriendEntity
import kotlinx.coroutines.flow.Flow

interface FriendLocalDataSource {
    suspend fun addFriend(friendEntity: FriendEntity): Response<Unit>
    suspend fun observeFriends(): Response<Flow<List<FriendEntity>>>
}