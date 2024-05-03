package com.ahmetocak.chat_box

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatBoxViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChatBoxUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private val messageValue get() = _uiState.value.messageValue

    fun onEvent(event: ChatBoxUiEvent) {
        when (event) {
            is ChatBoxUiEvent.OnMessageValueChange -> _uiState.update { it.copy(messageValue = event.value) }
            is ChatBoxUiEvent.OnChatDetailClick -> _navigationState.update {
                NavigationState.ChatDetail(event.id)
            }

            is ChatBoxUiEvent.OnViewChatDocsClick -> _navigationState.update {
                NavigationState.ChatDocuments(event.id)
            }

            is ChatBoxUiEvent.OnAttachDocClick -> _uiState.update { it.copy(showAttachDocBox = true) }
            is ChatBoxUiEvent.OnCameraClick -> _navigationState.update { NavigationState.Camera }
            is ChatBoxUiEvent.OnMicrophonePress -> _uiState.update { it.copy(activateMicrophone = true) }
            is ChatBoxUiEvent.OnCallClick -> { /* TODO: Start a call */ }
            is ChatBoxUiEvent.OnBackClick -> _navigationState.update { NavigationState.Back }
            is ChatBoxUiEvent.OnMenuClick -> _uiState.update { it.copy(showDropdownMenu = true) }
            is ChatBoxUiEvent.OnSendMessageClick -> { /* TODO: Send message */ }
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}