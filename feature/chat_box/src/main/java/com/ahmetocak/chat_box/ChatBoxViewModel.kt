package com.ahmetocak.chat_box

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ahmetocak.chat_box.audio.player.AudioPlayer
import com.ahmetocak.chat_box.audio.recorder.AudioRecorder
import com.ahmetocak.chat_box.navigation.CHAT_GROUP
import com.ahmetocak.common.MessageManager
import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.ext.decodeString
import com.ahmetocak.common.ext.encodeForSaveNav
import com.ahmetocak.domain.usecase.chat.AddMessageUseCase
import com.ahmetocak.domain.usecase.chat.GetAllMediaMessagesUseCase
import com.ahmetocak.domain.usecase.chat.GetMessagesUseCase
import com.ahmetocak.domain.usecase.chat.SendMessageUseCase
import com.ahmetocak.domain.usecase.chat_group.AddParticipantToGroupUseCase
import com.ahmetocak.domain.usecase.firebase.storage.UploadAudioFileUseCase
import com.ahmetocak.domain.usecase.firebase.storage.UploadDocFileUseCase
import com.ahmetocak.domain.usecase.firebase.storage.UploadImageFileUseCase
import com.ahmetocak.domain.usecase.user.local.ObserveUserInCacheUseCase
import com.ahmetocak.model.ChatGroup
import com.ahmetocak.model.LoadingState
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
    private val getAllMediaMessagesUseCase: GetAllMediaMessagesUseCase,
    private val addParticipantToGroupUseCase: AddParticipantToGroupUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatBoxUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    lateinit var groupData: ChatGroup
        private set

    init {
        val data = savedStateHandle.get<String>(CHAT_GROUP)
        if (data != null) {
            val decodedData = Json.decodeFromString<ChatGroup>(data)
            groupData = decodedData.copy(
                name = decodedData.name.decodeString(),
                participants = decodedData.participants.map {
                    it.copy(participantUsername = it.participantUsername.decodeString())
                }
            )
        }

        _uiState.update { state ->
            state.copy(
                imageUrl = groupData.imageUrl,
                title = groupData.name,
                messageList = getMessagesUseCase(friendshipId = groupData.id).cachedIn(
                    viewModelScope
                )
            )
        }

        audioPlayer.initializeMediaPlayer(onCompletion = {
            _uiState.update {
                it.copy(audioPlayStatus = AudioPlayStatus.IDLE)
            }
        })

        observeMessages()
        observeUser()
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
                            senderImgUrl = user.profilePicUrl.encodeForSaveNav(),
                            messageBoxId = groupData.id,
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

            is ChatBoxUiEvent.OnBackClick -> {
                when (uiState.value.screenState) {
                    is ScreenState.ChatBox -> _navigationState.update { NavigationState.Back }
                    is ScreenState.GroupInfo -> _uiState.update { it.copy(screenState = ScreenState.ChatBox) }
                    is ScreenState.GroupMedia -> _uiState.update { it.copy(screenState = ScreenState.ChatBox) }
                    is ScreenState.AddParticipant -> _uiState.update {
                        it.copy(parEmailVal = "", screenState = ScreenState.GroupInfo)
                    }
                }
            }

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

            is ChatBoxUiEvent.OnGroupInfoClick -> {
                _uiState.update {
                    it.copy(screenState = ScreenState.GroupInfo)
                }
                getMediaMessages()
            }

            is ChatBoxUiEvent.OnGroupMediaClick -> {
                _uiState.update {
                    it.copy(screenState = ScreenState.GroupMedia)
                }
                getMediaMessages()
            }

            is ChatBoxUiEvent.UpdateGroupImage -> updateGroupImage(event.uri)

            is ChatBoxUiEvent.OnAddParticipantClick -> addParticipant()
            is ChatBoxUiEvent.ShowAddParticipantView -> _uiState.update {
                it.copy(screenState = ScreenState.AddParticipant)
            }

            is ChatBoxUiEvent.OnParticipantValChange -> {
                _uiState.update { it.copy(parEmailVal = event.value) }
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
        viewModelScope.launch(ioDispatcher) {
            sendMessageUseCase(
                message = Message(
                    senderEmail = _uiState.value.currentUser?.email ?: "",
                    messageContent = messageContent,
                    senderImgUrl = _uiState.value.currentUser?.profilePicUrl,
                    senderUsername = _uiState.value.currentUser?.username ?: "",
                    messageType = messageType,
                    messageBoxId = groupData.id
                ),
                onFailure = {
                    SnackbarManager.showMessage(it)
                }
            )
        }
        _uiState.update {
            it.copy(messageValue = "")
        }
    }

    private fun getMediaMessages() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = getAllMediaMessagesUseCase(groupData.id)) {
                is Response.Success -> {
                    _uiState.update {
                        it.copy(
                            mediaMessages = response.data,
                            loadingState = LoadingState.Idle
                        )
                    }
                }

                is Response.Error -> {
                    _uiState.update { it.copy(loadingState = LoadingState.Idle) }
                    SnackbarManager.showMessage(response.errorMessage)
                }
            }
        }
    }

    private fun addParticipant() {
        _uiState.update { it.copy(loadingState = LoadingState.Loading) }
        viewModelScope.launch(ioDispatcher) {
            when (val response = addParticipantToGroupUseCase(groupData.id, uiState.value.parEmailVal)) {
                is Response.Success -> _uiState.update { it.copy(loadingState = LoadingState.Idle) }
                is Response.Error -> {
                    _uiState.update { it.copy(loadingState = LoadingState.Idle) }
                    SnackbarManager.showMessage(response.errorMessage)
                }
            }
        }
    }

    private fun updateGroupImage(uri: Uri) {
        TODO("Not yet implemented")
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