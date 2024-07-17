package com.ahmetocak.data.di

import com.ahmetocak.data.repository.chat.ChatRepository
import com.ahmetocak.data.repository.chat.ChatRepositoryImpl
import com.ahmetocak.data.repository.chat_group.ChatGroupRepository
import com.ahmetocak.data.repository.chat_group.ChatGroupRepositoryImpl
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.ahmetocak.data.repository.firebase.storage.StorageRepositoryImpl
import com.ahmetocak.data.repository.user.UserRepository
import com.ahmetocak.data.repository.user.UserRepositoryImpl
import com.ahmetocak.database.datasource.chat_group.ChatGroupLocalDataSource
import com.ahmetocak.database.datasource.message.MessageLocalDataSource
import com.ahmetocak.database.datasource.user.UserLocalDataSource
import com.ahmetocak.database.db.UserDatabase
import com.ahmetocak.network.api.KtorChatApi
import com.ahmetocak.network.api.chat.ChatService
import com.ahmetocak.network.datasource.firebase.storage.StorageRemoteDataSource
import com.ahmetocak.network.datasource.ktor_chat_group.ChatGroupRemoteDataSource
import com.ahmetocak.network.datasource.ktor_user.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideChatRepository(
        chatService: ChatService,
        api: KtorChatApi,
        db: UserDatabase,
        messageLocalDataSource: MessageLocalDataSource
    ): ChatRepository {
        return ChatRepositoryImpl(chatService, api, db, messageLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideKtorUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        userLocalDataSource: UserLocalDataSource
    ): UserRepository {
        return UserRepositoryImpl(
            userRemoteDataSource = userRemoteDataSource,
            userLocalDataSource = userLocalDataSource
        )
    }

    @Singleton
    @Provides
    fun provideStorageRepository(
        storageRemoteDataSource: StorageRemoteDataSource
    ): StorageRepository {
        return StorageRepositoryImpl(storageRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideChatGroupRepository(
        localDataSource: ChatGroupLocalDataSource,
        remoteDataSource: ChatGroupRemoteDataSource
    ): ChatGroupRepository {
        return ChatGroupRepositoryImpl(remoteDataSource, localDataSource)
    }
}