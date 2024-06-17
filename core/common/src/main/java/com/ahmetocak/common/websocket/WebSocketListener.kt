package com.ahmetocak.common.websocket

import android.util.Log
import com.ahmetocak.common.MessageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener : WebSocketListener() {

    companion object {
        private val _isConnected = MutableStateFlow(false)
        val isConnected get() = _isConnected.asStateFlow()
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        _isConnected.update { true }
        Log.d("WEB SOCKET", "onOpen ${response.message}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        _isConnected.update { true }
        MessageManager.emitMessage(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        _isConnected.update { false }
        Log.d("WEB SOCKET", "onClosing $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        _isConnected.update { false }
        Log.d("WEB SOCKET", "onClosed $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        _isConnected.update { false }
        Log.d("WEB SOCKET", t.stackTraceToString())
    }
}