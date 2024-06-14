package com.ahmetocak.chatapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.websocket.WebSocketManager
import com.ahmetocak.domain.usecase.app_preferences.GetAppThemeUseCase
import com.ahmetocak.domain.usecase.app_preferences.GetDynamicColorUseCase
import com.ahmetocak.domain.usecase.user.local.GetUserEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val getDynamicColorUseCase: GetDynamicColorUseCase,
    private val getUserEmailUseCase: GetUserEmailUseCase,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow(MainActivityUiState())
    val uiState = _uiState.asStateFlow()

    init {
        initializeTheme()
        observeAppTheme()
        observeDynamicColor()
        connectToWebsocket()
    }

    private fun initializeTheme() {
        runBlocking(ioDispatcher) {
            _uiState.update {
                it.copy(
                    isDarkMode = getAppThemeUseCase().first(),
                    isDynamicColor = getDynamicColorUseCase().first()
                )
            }
        }
    }

    private fun observeAppTheme() {
        viewModelScope.launch(ioDispatcher) {
            getAppThemeUseCase().collect { isDarkMode ->
                _uiState.update { it.copy(isDarkMode = isDarkMode) }
            }
        }
    }

    private fun observeDynamicColor() {
        viewModelScope.launch(ioDispatcher) {
            getDynamicColorUseCase().collect { isDynamicColor ->
                _uiState.update { it.copy(isDynamicColor = isDynamicColor) }
            }
        }
    }

    private fun connectToWebsocket() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getUserEmailUseCase()) {
                is Response.Success -> WebSocketManager.initializeWebsocket(response.data)
                is Response.Error -> SnackbarManager.showMessage(response.errorMessage)
            }
        }
    }
}

data class MainActivityUiState(
    val isDarkMode: Boolean = false,
    val isDynamicColor: Boolean = false
)