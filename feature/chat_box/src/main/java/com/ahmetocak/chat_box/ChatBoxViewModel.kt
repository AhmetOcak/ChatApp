package com.ahmetocak.chat_box

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.MessageManager
import com.ahmetocak.common.ext.getCurrentDate
import com.ahmetocak.domain.usecase.chat.SendMessageUseCase
import com.ahmetocak.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ChatBoxViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatBoxUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private val messageValue get() = _uiState.value.messageValue

    init {
        viewModelScope.launch(Dispatchers.IO) {
            MessageManager.messages.collect { messageText ->
                if (messageText != null) {
                    addMessageToChatList(Json.decodeFromString<Message>(messageText))
                }
            }
        }
    }

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
            is ChatBoxUiEvent.OnSendMessageClick -> { sendMessage() }
        }
    }

    private fun sendMessage() {
        val message = Message(
            authorId = "1",
            authorImage = "https://fastly.picsum.photos/id/367/200/200.jpg?hmac=6NmiWxiENMBIeAXEfu9fN20uigiBudgYzqHfz-eXZYk",
            authorName = "Ahmet Ocak",
            message = messageValue,
            time = getCurrentDate()
        )

        sendMessageUseCase(message = message, receiverId = "2")
        addMessageToChatList(message)

        _uiState.update {
            it.copy(messageValue = "")
        }
    }

    private fun addMessageToChatList(message: Message) {
        val chat = _uiState.value.chat.toMutableList()
        chat.add(message)
        _uiState.update {
            it.copy(chat = chat)
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}