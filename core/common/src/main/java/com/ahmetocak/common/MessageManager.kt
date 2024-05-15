package com.ahmetocak.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object MessageManager {
    private val _messages = MutableStateFlow<String?>(null)
    val messages get() = _messages.asStateFlow()

    fun emitMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _messages.emit(message)
        }
    }
}