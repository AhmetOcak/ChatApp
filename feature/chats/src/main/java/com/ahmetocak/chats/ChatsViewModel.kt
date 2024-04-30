package com.ahmetocak.chats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChatsUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private val chatImageClickState get() = _uiState.value.chatImageClickState

    fun onEvent(event: ChatsUiEvent) {
        when (event) {
            is ChatsUiEvent.OnLoadingStateChange -> _uiState.update { it.copy(loadingState = event.state) }
            is ChatsUiEvent.OnChatItemClick -> _navigationState.update {
                NavigationState.ChatBox(
                    event.id
                )
            }

            is ChatsUiEvent.OnImageClick -> {
                when (chatImageClickState) {
                    is ChatImageClickState.Idle -> _uiState.update {
                        it.copy(chatImageClickState = ChatImageClickState.ShowMiniChatMenu)
                    }

                    is ChatImageClickState.ShowMiniChatMenu -> _uiState.update {
                        it.copy(
                            chatImageClickState = ChatImageClickState.ShowFullScreenImage
                        )
                    }

                    is ChatImageClickState.ShowFullScreenImage -> Unit
                }
            }
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }

    fun resetChatImageClickState() {
        _uiState.update { it.copy(chatImageClickState = ChatImageClickState.Idle) }
    }
}