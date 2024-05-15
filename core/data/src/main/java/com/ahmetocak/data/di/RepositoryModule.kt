package com.ahmetocak.data.di

import com.ahmetocak.data.repository.chat.ChatRepository
import com.ahmetocak.data.repository.chat.ChatRepositoryImpl
import com.ahmetocak.network.api.chat.ChatService
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
}