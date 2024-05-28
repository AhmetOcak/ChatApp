package com.ahmetocak.data.di

import com.ahmetocak.data.repository.chat.ChatRepository
import com.ahmetocak.data.repository.chat.ChatRepositoryImpl
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.ahmetocak.data.repository.firebase.storage.StorageRepositoryImpl
import com.ahmetocak.data.repository.friend.FriendRepository
import com.ahmetocak.data.repository.friend.FriendRepositoryImpl
import com.ahmetocak.data.repository.user.UserRepository
import com.ahmetocak.data.repository.user.UserRepositoryImpl
import com.ahmetocak.database.datasource.friend.FriendLocalDataSource
import com.ahmetocak.database.datasource.user.UserLocalDataSource
import com.ahmetocak.network.api.chat.ChatService
import com.ahmetocak.network.datasource.firebase.storage.StorageRemoteDataSource
import com.ahmetocak.network.datasource.ktor_friend.FriendRemoteDataSource
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
    fun provideChatRepository(chatService: ChatService): ChatRepository {
        return ChatRepositoryImpl(chatService)
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
    fun provideFriendRepository(
        friendRemoteDataSource: FriendRemoteDataSource,
        friendLocalDataSource: FriendLocalDataSource
    ): FriendRepository {
        return FriendRepositoryImpl(friendRemoteDataSource, friendLocalDataSource)
    }
}