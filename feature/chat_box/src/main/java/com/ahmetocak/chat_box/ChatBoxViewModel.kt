package com.ahmetocak.chat_box

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ahmetocak.chat_box.audio.player.AudioPlayer
import com.ahmetocak.chat_box.navigation.CHAT_BOX_ID
import com.ahmetocak.chat_box.navigation.FRIEND_EMAIL
import com.ahmetocak.chat_box.navigation.FRIEND_PROF_PIC_URL
import com.ahmetocak.chat_box.navigation.FRIEND_USERNAME
import com.ahmetocak.chat_box.audio.recorder.AudioRecorder
import com.ahmetocak.common.MessageManager
import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.domain.usecase.chat.AddMessageUseCase
import com.ahmetocak.domain.usecase.chat.GetMessagesUseCase
import com.ahmetocak.domain.usecase.chat.SendMessageUseCase
import com.ahmetocak.domain.usecase.user.local.ObserveUserInCacheUseCase
import com.ahmetocak.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ChatBoxViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val addMessageUseCase: AddMessageUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val observeUserInCacheUseCase: ObserveUserInCacheUseCase,
    private val dispatcher: CoroutineDispatcher,
    private val audioRecorder: AudioRecorder,
    private val audioPlayer: AudioPlayer,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatBoxUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    init {
        observeMessages()
        observeUser()

        val chatBoxId = savedStateHandle.get<String>(CHAT_BOX_ID)
        val friendEmail = savedStateHandle.get<String>(FRIEND_EMAIL)
        val friendUsername = savedStateHandle.get<String>(FRIEND_USERNAME)
        val friendProfilePicUrl = savedStateHandle.get<String?>(FRIEND_PROF_PIC_URL)

        _uiState.update {
            it.copy(
                title = friendUsername ?: friendEmail ?: "",
                imageUrl = friendProfilePicUrl,
                messageList = getMessagesUseCase(
                    userEmail = _uiState.value.currentUser?.email ?: "",
                    friendEmail = friendEmail ?: ""
                ).cachedIn(viewModelScope)
            )
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
            is ChatBoxUiEvent.OnMicrophonePress -> {
                if (event.permission()) {
                    when (_uiState.value.audioRecordStatus) {
                        AudioRecordStatus.IDLE -> {
                            _uiState.update {
                                it.copy(audioRecordStatus = AudioRecordStatus.RECORDING)
                            }
                            _uiState.value.currentUser?.email?.let { email ->
                                audioRecorder.startRecording(
                                    userEmail = email
                                )
                            }
                        }

                        AudioRecordStatus.RECORDING -> {
                            _uiState.update {
                                it.copy(audioRecordStatus = AudioRecordStatus.IDLE)
                            }
                            audioRecorder.stopRecording()
                        }
                    }
                }
            }

            is ChatBoxUiEvent.OnCallClick -> { /* TODO: Start a call */
            }

            is ChatBoxUiEvent.OnBackClick -> _navigationState.update { NavigationState.Back }
            is ChatBoxUiEvent.OnMenuClick -> _uiState.update { it.copy(showDropdownMenu = true) }
            is ChatBoxUiEvent.OnSendMessageClick -> {
                sendMessage()
            }
        }
    }

    private fun observeMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            MessageManager.messages.collect { messageText ->
                if (messageText != null) {
                    val message = Json.decodeFromString<Message>(messageText)
                    when (val response = addMessageUseCase(message = message)) {
                        is Response.Success -> {}
                        is Response.Error -> SnackbarManager.showMessage(response.errorMessage)
                    }
                }
            }
        }
    }

    private fun observeUser() {
        viewModelScope.launch(dispatcher) {
            when (val response = observeUserInCacheUseCase()) {
                is Response.Success -> {
                    response.data.collect { user ->
                        if (user == null) return@collect
                        _uiState.update { it.copy(currentUser = user) }
                    }
                }

                is Response.Error -> SnackbarManager.showMessage(response.errorMessage)
            }
        }
    }

    private fun sendMessage() {
        sendMessageUseCase(
            message = Message(
                senderEmail = _uiState.value.currentUser?.email ?: "",
                receiverEmail = _uiState.value.title,
                messageText = _uiState.value.messageValue,
                senderImgUrl = _uiState.value.currentUser?.profilePicUrl,
                senderUsername = _uiState.value.currentUser?.username ?: ""
            )
        )
        _uiState.update {
            it.copy(messageValue = "")
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}