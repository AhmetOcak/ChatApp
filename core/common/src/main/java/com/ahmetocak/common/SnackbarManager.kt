package com.ahmetocak.common

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object SnackbarManager {
    private val _messages = MutableStateFlow<Pair<UiText, SnackbarDuration?>?>(null)
    val messages get() = _messages.asStateFlow()

    fun showMessage(message: UiText, duration: SnackbarDuration? = null) {
        _messages.update { Pair(message, duration) }
    }

    fun clean() {
        _messages.update { null }
    }
}