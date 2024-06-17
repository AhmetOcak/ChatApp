package com.ahmetocak.common.websocket

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

object WebSocketManager {

    private const val BASE_URL = "ws://192.168.1.6:8080/chat"
    private var currentUserEmail: String? = null
    private lateinit var webSocket: WebSocket

    fun initializeWebsocket(currentUserEmail: String?) {
        if (this.currentUserEmail != null) {
            webSocket = OkHttpClient().newWebSocket(
                Request.Builder()
                    .url("$BASE_URL/$currentUserEmail")
                    .build(),
                WebSocketListener()
            )
        } else {
            this.currentUserEmail = currentUserEmail
            initializeWebsocket(this.currentUserEmail)
        }
    }

    fun getWebsocketInstance(): WebSocket {
        return if (this::webSocket.isInitialized) {
            webSocket
        } else {
            initializeWebsocket(this.currentUserEmail)
            webSocket
        }
    }
}