package com.ahmetocak.data.repository.chat

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ahmetocak.common.Response
import com.ahmetocak.common.mapResponse
import com.ahmetocak.data.mapper.toMessage
import com.ahmetocak.data.mapper.toMessageEntity
import com.ahmetocak.data.mapper.toNetworkMessage
import com.ahmetocak.data.mediator.MessagesRemoteMediator
import com.ahmetocak.database.datasource.message.MessageLocalDataSource
import com.ahmetocak.database.db.UserDatabase
import com.ahmetocak.model.Message
import com.ahmetocak.network.api.KtorChatApi
import com.ahmetocak.network.api.chat.ChatService
import com.ahmetocak.network.datasource.messages.MessagesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService,
    private val api: KtorChatApi,
    private val db: UserDatabase,
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messagesRemoteDataSource: MessagesRemoteDataSource
) : ChatRepository {

    override fun sendMessageWithWebSocket(message: Message) {
        chatService.sendMessageWithWebSocket(message.toNetworkMessage())
    }

    override suspend fun sendMessageWithoutWebSocket(message: Message): Response<Message> {
        return chatService.sendMessageWithoutWebSocket(
            message.toNetworkMessage()
        ).mapResponse { it.toMessage() }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMessages(friendshipId: Int): Flow<PagingData<Message>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MessagesRemoteMediator(
                friendshipId = friendshipId,
                api = api,
                userDb = db
            ),
            pagingSourceFactory = { db.messageDao().pagingSource(friendshipId) }
        ).flow.map { messageEntity ->
            messageEntity.map { it.toMessage() }
        }
    }

    override suspend fun addMessage(message: Message): Response<Unit> {
        return messageLocalDataSource.addMessage(messageEntity = message.toMessageEntity())
    }

    override suspend fun getAllMediaMessages(messageBoxId: Int): Response<List<Message>> {
        return messagesRemoteDataSource.getAllMediaMessages(messageBoxId)
            .mapResponse { list -> list.map { it.toMessage() } }
    }
}