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
    private val userEmail: String,
    private val friendEmail: String,
    private val api: KtorChatApi,
    private val userDb: UserDatabase
) : RemoteMediator<Int, MessageEntity>() {

    private val messageDao = userDb.messageDao()
    private val remoteKeyDao = userDb.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val remoteKey = userDb.withTransaction {
            remoteKeyDao.getRemoteKey()
        }

        return if (remoteKey == null) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = userDb.withTransaction {
                        remoteKeyDao.getRemoteKey()
                    } ?: return MediatorResult.Success(endOfPaginationReached = true)

                    remoteKey.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = api.getMessages(
                page = page,
                userEmail = userEmail,
                friendEmail = friendEmail
            )

            userDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearKey()
                }

                val nextPage = if (response.messageList.isEmpty()) {
                    null
                } else page+1

                remoteKeyDao.insertKey(
                    RemoteKeyEntity(id = "messages_key", nextPage = nextPage)
                )
                messageDao.insertAll(response.messageList.toListMessageEntity())
            }

            MediatorResult.Success(endOfPaginationReached = response.messageList.isEmpty())
        } catch (e: Exception) {
            Log.d("mediator", e.stackTraceToString())
            return MediatorResult.Error(e)
        }
    }
}