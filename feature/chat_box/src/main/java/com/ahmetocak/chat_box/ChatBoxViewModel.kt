package com.ahmetocak.chat_box

import androidx.core.net.toUri
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
import com.ahmetocak.domain.usecase.firebase.storage.UploadAudioFileUseCase
import com.ahmetocak.domain.usecase.firebase.storage.UploadDocFileUseCase
import com.ahmetocak.domain.usecase.firebase.storage.UploadImageFileUseCase
import com.ahmetocak.domain.usecase.user.local.ObserveUserInCacheUseCase
import com.ahmetocak.model.Message
import com.ahmetocak.model.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
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
    private val uploadAudioFileUseCase: UploadAudioFileUseCase,
    private val uploadImageFileUseCase: UploadImageFileUseCase,
    private val uploadDocFileUseCase: UploadDocFileUseCase,
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

        audioPlayer.initializeMediaPlayer(onCompletion = {
            _uiState.update {
                it.copy(audioPlayStatus = AudioPlayStatus.IDLE)
            }
        })
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

            is ChatBoxUiEvent.OnAttachMenuClick -> _uiState.update {
                it.copy(showAttachMenu = !_uiState.value.showAttachMenu)
            }

            is ChatBoxUiEvent.OnCameraClick -> {
                _uiState.value.currentUser?.let { user ->
                    _navigationState.update {
                        NavigationState.Camera(
                            senderEmail = user.email,
                            senderUsername = user.username,
                            senderImgUrl = user.profilePicUrl,
                            receiverEmail = _uiState.value.title
                        )
                    }
                }
            }

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
                            val audioFile = audioRecorder.stopRecording()

                            uploadAudioFileUseCase(
                                audioFileUri = audioFile.toUri(),
                                audioFileName = audioFile.name,
                                onSuccess = { audioFileUrl ->
                                    sendMessage(
                                        messageType = MessageType.AUDIO,
                                        messageContent = audioFileUrl.toString()
                                    )
                                },
                                onFailure = {
                                    SnackbarManager.showMessage(it)
                                }
                            )
                        }
                    }
                }
            }

            is ChatBoxUiEvent.OnCallClick -> { /* TODO: Start a call */
            }

            is ChatBoxUiEvent.OnBackClick -> _navigationState.update { NavigationState.Back }
            is ChatBoxUiEvent.OnMenuClick -> _uiState.update { it.copy(showDropdownMenu = true) }
            is ChatBoxUiEvent.OnSendMessageClick -> sendMessage(
                messageType = event.messageType,
                messageContent = _uiState.value.messageValue
            )

            is ChatBoxUiEvent.OnPlayAudioClick -> {
                when (_uiState.value.audioPlayStatus) {
                    AudioPlayStatus.PLAYING -> {
                        _uiState.update { it.copy(audioPlayStatus = AudioPlayStatus.IDLE) }
                        audioPlayer.stop()
                    }

                    AudioPlayStatus.IDLE -> {
                        _uiState.update { it.copy(audioPlayStatus = AudioPlayStatus.PLAYING) }
                        audioPlayer.play(event.audioUrl.toString())
                    }
                }
            }

            is ChatBoxUiEvent.OnSendImageClick -> {
                uploadImageFileUseCase(
                    imageFileName = "${_uiState.value.currentUser?.email}${LocalDateTime.now()}",
                    imageFileUri = event.imageUri,
                    onSuccess = {
                        sendMessage(
                            messageType = MessageType.IMAGE,
                            messageContent = it.toString()
                        )
                    },
                    onFailure = {
                        SnackbarManager.showMessage(it)
                    }
                )
                _uiState.update { it.copy(showAttachMenu = false) }
            }

            is ChatBoxUiEvent.OnSendDocClick -> {
                uploadDocFileUseCase(
                    docFileName = "${_uiState.value.currentUser?.email}${LocalDateTime.now()}",
                    docFileUri = event.docUri,
                    onSuccess = {
                        sendMessage(
                            messageType = MessageType.DOC,
                            messageContent = it.toString()
                        )
                    },
                    onFailure = {
                        SnackbarManager.showMessage(it)
                    }
                )
                _uiState.update { it.copy(showAttachMenu = false) }
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

    private fun sendMessage(messageType: MessageType, messageContent: String) {
        sendMessageUseCase(
            message = Message(
                senderEmail = _uiState.value.currentUser?.email ?: "",
                receiverEmail = _uiState.value.title,
                messageContent = messageContent,
                senderImgUrl = _uiState.value.currentUser?.profilePicUrl,
                senderUsername = _uiState.value.currentUser?.username ?: "",
                messageType = messageType
            )
        )
        _uiState.update {
            it.copy(messageValue = "")
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }

    fun resetAttachMenu() = _uiState.update { it.copy(showAttachMenu = false) }

    override fun onCleared() {
        audioPlayer.releaseMediaPlayer()
        super.onCleared()
    }
}