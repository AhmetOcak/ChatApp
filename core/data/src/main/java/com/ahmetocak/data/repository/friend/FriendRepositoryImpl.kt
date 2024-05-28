package com.ahmetocak.data.repository.friend

import com.ahmetocak.common.Response
import com.ahmetocak.common.mapResponse
import com.ahmetocak.data.mapper.toFriend
import com.ahmetocak.data.mapper.toFriendEntity
import com.ahmetocak.data.mapper.toListFriend
import com.ahmetocak.database.datasource.friend.FriendLocalDataSource
import com.ahmetocak.model.Friend
import com.ahmetocak.network.datasource.ktor_friend.FriendRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendRemoteDataSource: FriendRemoteDataSource,
    private val friendLocalDataSource: FriendLocalDataSource
) : FriendRepository {
    override suspend fun getFriends(userEmail: String): Response<List<Friend>> {
        return friendRemoteDataSource.getFriends(userEmail).mapResponse { it.toFriend() }
    }

    override suspend fun createFriend(userEmail: String, friendEmail: String): Response<Friend> {
        return friendRemoteDataSource.createFriend(
            userEmail = userEmail, friendEmail = friendEmail
        ).mapResponse { it.toFriend() }
    }

    override suspend fun addFriendToCache(friend: Friend): Response<Unit> {
        return friendLocalDataSource.addFriend(friend.toFriendEntity())
    }

    override suspend fun observeFriendInCache(): Response<Flow<List<Friend>>> {
        return friendLocalDataSource.observeFriends()
            .mapResponse { it.map { entity -> entity.toListFriend() } }
    }
}