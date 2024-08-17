package com.ahmetocak.chat_box

import android.content.Context
import android.net.Uri
import androidx.paging.PagingData
import com.ahmetocak.model.LoadingState
import com.ahmetocak.model.Message
import com.ahmetocak.model.MessageType
import com.ahmetocak.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ChatBoxUiState(
    val screenState: ScreenState = ScreenState.ChatBox,
    val messageValue: String = "",
    val parEmailVal: String = "",
    val messageList: Flow<PagingData<Message>> = emptyFlow(),
    val title: String = "",
    val imageUrl: String? = null,
    val audioRecordStatus: AudioRecordStatus = AudioRecordStatus.IDLE,
    val currentUser: User? = null,
    val audioPlayStatus: AudioPlayStatus = AudioPlayStatus.IDLE,
    val showAttachMenu: Boolean = false,
    val mediaMessages: List<Message> = emptyList(),
    val loadingState: LoadingState = LoadingState.Loading
)

sealed class ChatBoxUiEvent {
    data class OnMessageValueChange(val value: String) : ChatBoxUiEvent()
    data object OnAttachMenuClick : ChatBoxUiEvent()
    data object OnCameraClick : ChatBoxUiEvent()

    data class OnMicrophonePress(
        val context: Context,
        val permission: () -> Boolean
    ) : ChatBoxUiEvent()

    data object OnBackClick : ChatBoxUiEvent()
    data class OnChatDetailClick(val id: String) : ChatBoxUiEvent()
    data class OnSendMessageClick(val messageType: MessageType) : ChatBoxUiEvent()
    data class OnViewChatDocsClick(val id: String) : ChatBoxUiEvent()
    data class OnPlayAudioClick(val audioUrl: Uri) : ChatBoxUiEvent()
    data class OnSendImageClick(val imageUri: Uri) : ChatBoxUiEvent()
    data class OnSendDocClick(val docUri: Uri) : ChatBoxUiEvent()
    data object OnGroupInfoClick : ChatBoxUiEvent()
    data object OnGroupMediaClick : ChatBoxUiEvent()
    data class UpdateGroupImage(val uri: Uri) : ChatBoxUiEvent()
    data object ShowAddParticipantView  : ChatBoxUiEvent()
    data object OnAddParticipantClick : ChatBoxUiEvent()
    data class OnParticipantValChange(val value: String) : ChatBoxUiEvent()
}

sealed class NavigationState {
    data object None : NavigationState()
    data class ChatDetail(val id: String) : NavigationState()
    data class ChatDocuments(val id: String) : NavigationState()
    data class Camera(
        val messageBoxId: Int,
        val senderEmail: String,
        val senderImgUrl: String?,
        val senderUsername: String
    ) : NavigationState()
    data object Back : NavigationState()
}

sealed interface AudioRecordStatus {
    data object RECORDING : AudioRecordStatus
    data object IDLE : AudioRecordStatus
}

sealed interface AudioPlayStatus {
    data object PLAYING : AudioPlayStatus
    data object IDLE : AudioPlayStatus
}

sealed interface ScreenState {
    data object ChatBox : ScreenState
    data object GroupInfo : ScreenState
    data object GroupMedia : ScreenState
    data object AddParticipant : ScreenState
}