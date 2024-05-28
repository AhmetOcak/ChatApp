package com.ahmetocak.database.datasource.friend

import com.ahmetocak.common.Response
import com.ahmetocak.database.dao.FriendDao
import com.ahmetocak.database.entity.FriendEntity
import com.ahmetocak.database.utils.dbCall
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FriendLocalDataSourceImpl @Inject constructor(
    private val dao: FriendDao
) : FriendLocalDataSource {
    override suspend fun addFriend(friendEntity: FriendEntity): Response<Unit> {
        return dbCall { dao.addFriend(friendEntity) }
    }

    override suspend fun observeFriends(): Response<Flow<List<FriendEntity>>> {
        return dbCall { dao.observeFriends() }
    }
}