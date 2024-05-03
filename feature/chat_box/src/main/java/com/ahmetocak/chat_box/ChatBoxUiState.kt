package com.ahmetocak.chat_box

import androidx.paging.PagingData
import com.ahmetocak.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ChatBoxUiState(
    val messageValue: String = "",
    val chat: Flow<PagingData<Message>> = emptyFlow(),
    val members: String = "",
    val title: String = "",
    val showDropdownMenu: Boolean = false,
    val imageUrl: String? = null,
    val showAttachDocBox: Boolean = false,
    val activateMicrophone: Boolean = false
)

sealed class ChatBoxUiEvent {
    data class OnMessageValueChange(val value: String) : ChatBoxUiEvent()
    data object OnAttachDocClick : ChatBoxUiEvent()
    data object OnCameraClick : ChatBoxUiEvent()
    data object OnMicrophonePress : ChatBoxUiEvent()
    data object OnCallClick : ChatBoxUiEvent()
    data object OnBackClick : ChatBoxUiEvent()
    data class OnChatDetailClick(val id: String) : ChatBoxUiEvent()
    data object OnMenuClick : ChatBoxUiEvent()
    data object OnSendMessageClick: ChatBoxUiEvent()
    data class OnViewChatDocsClick(val id: String) : ChatBoxUiEvent()
}

sealed class NavigationState {
    data object None : NavigationState()
    data class ChatDetail(val id: String) : NavigationState()
    data class ChatDocuments(val id: String) : NavigationState()
    data object Camera : NavigationState()
    data object Back : NavigationState()
}