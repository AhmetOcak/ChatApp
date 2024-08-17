package com.ahmetocak.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ahmetocak.data.mapper.toListMessageEntity
import com.ahmetocak.database.db.UserDatabase
import com.ahmetocak.database.entity.MessageEntity
import com.ahmetocak.database.entity.RemoteKeyEntity
import com.ahmetocak.network.api.KtorChatApi

@OptIn(ExperimentalPagingApi::class)
class MessagesRemoteMediator(
    private val friendshipId: Int,
    private val api: KtorChatApi,
    private val userDb: UserDatabase
) : RemoteMediator<Int, MessageEntity>() {

    private val messageDao = userDb.messageDao()
    private val remoteKeyDao = userDb.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val remoteKey = userDb.withTransaction {
                        remoteKeyDao.getRemoteKey(friendshipId)
                    } ?: return MediatorResult.Success(endOfPaginationReached = true)

                    remoteKey.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                }
            }

            val response = api.getMessages(
                page = page,
                messageBoxId = friendshipId
            )

            userDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearKey(friendshipId)
                }

                val nextPage = if (response.messageList.isEmpty()) {
                    null
                } else page + 1

                remoteKeyDao.insertKey(
                    RemoteKeyEntity(
                        nextPage = nextPage,
                        friendshipId = friendshipId
                    )
                )
                messageDao.insertAll(response.messageList.toListMessageEntity())
            }

            MediatorResult.Success(
                endOfPaginationReached = response.messageList.isEmpty() || response.messageList.size != 20
            )
        } catch (e: Exception) {
            Log.d("MessagesRemoteMediator", e.stackTraceToString())
            return MediatorResult.Error(e)
        }
    }
}