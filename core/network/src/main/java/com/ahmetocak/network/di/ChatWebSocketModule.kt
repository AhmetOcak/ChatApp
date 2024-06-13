package com.ahmetocak.network.di

import com.ahmetocak.network.api.chat.ChatService
import com.ahmetocak.network.api.chat.ChatServiceImpl
import com.ahmetocak.network.api.chat.WebSocketListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatWebSocketModule {

    @Singleton
    @Provides
    fun provideWebSocket(): WebSocket {
        return OkHttpClient().newWebSocket(
            Request.Builder()
                .url("ws://10.0.2.2:8080/chat/xana@gmail.com")
                .build(),
            WebSocketListener()
        )
    }

    @Singleton
    @Provides
    fun provideChatWebSocketImpl(webSocket: WebSocket): ChatService {
        return ChatServiceImpl(webSocket)
    }
}