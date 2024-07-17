package com.ahmetocak.chat_box

import android.content.Context
import android.net.Uri
import androidx.paging.PagingData
import com.ahmetocak.model.Message
import com.ahmetocak.model.MessageType
import com.ahmetocak.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ChatBoxUiState(
    val messageValue: String = "",
    val messageList: Flow<PagingData<Message>> = emptyFlow(),
    val title: String = "",
    val showDropdownMenu: Boolean = false,
    val imageUrl: String? = null,
    val audioRecordStatus: AudioRecordStatus = AudioRecordStatus.IDLE,
    val currentUser: User? = null,
    val audioPlayStatus: AudioPlayStatus = AudioPlayStatus.IDLE,
    val showAttachMenu: Boolean = false
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
    data object OnMenuClick : ChatBoxUiEvent()
    data class OnSendMessageClick(val messageType: MessageType) : ChatBoxUiEvent()
    data class OnViewChatDocsClick(val id: String) : ChatBoxUiEvent()
    data class OnPlayAudioClick(val audioUrl: Uri) : ChatBoxUiEvent()
    data class OnSendImageClick(val imageUri: Uri) : ChatBoxUiEvent()
    data class OnSendDocClick(val docUri: Uri) : ChatBoxUiEvent()
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