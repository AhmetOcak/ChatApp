package com.ahmetocak.common.websocket

import android.util.Log
import com.ahmetocak.common.MessageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener : WebSocketListener() {

    companion object {
        private val _isConnected = MutableStateFlow(Connection.NOT_CONNECTED)
        val isConnected get() = _isConnected.asStateFlow()

        private fun emit(connection: Connection) {
            CoroutineScope(Dispatchers.IO).launch {
                _isConnected.emit(connection)
            }
        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        emit(Connection.CONNECTED)
        Log.d("WEB SOCKET", "onOpen ${response.message}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        emit(Connection.CONNECTED)
        MessageManager.emitMessage(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        emit(Connection.NOT_CONNECTED)
        Log.d("WEB SOCKET", "onClosing $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        emit(Connection.NOT_CONNECTED)
        Log.d("WEB SOCKET", "onClosed $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        emit(Connection.NOT_CONNECTED)
        Log.d("WEB SOCKET", t.stackTraceToString())
    }
}

enum class Connection {
    CONNECTED,
    NOT_CONNECTED
}