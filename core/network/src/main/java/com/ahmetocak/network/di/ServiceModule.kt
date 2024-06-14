package com.ahmetocak.network.di

import com.ahmetocak.network.api.chat.ChatService
import com.ahmetocak.network.api.chat.ChatServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideChatService(): ChatService {
        return ChatServiceImpl()
    }
}