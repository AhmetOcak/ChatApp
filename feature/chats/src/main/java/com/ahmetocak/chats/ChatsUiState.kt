package com.ahmetocak.chats

import android.net.Uri
import com.ahmetocak.common.UiText
import com.ahmetocak.model.ChatGroup
import com.ahmetocak.model.ChatGroupParticipants
import com.ahmetocak.model.LoadingState

data class ChatsUiState(
    val textFieldValue: String = "",
    val searchValue: String = "",
    val selectedGroupImgUri: Uri? = null,
    val chatList: List<ChatGroup> = emptyList(),
    val participantList: List<ChatGroupParticipants> = emptyList(),
    val selectedParticipants: List<ChatGroupParticipants> = emptyList(),
    val screenState: ScreenState = ScreenState.Chats,
    val loadingState: LoadingState = LoadingState.Idle
)

sealed class ChatsUiEvent {
    data class OnChatItemClick(val chatGroup: ChatGroup) : ChatsUiEvent()
    data class OnValueChanged(val value: String) : ChatsUiEvent()
    data class OnSearchValueChanged(val value: String) : ChatsUiEvent()
    data object OnSettingsClick : ChatsUiEvent()
    data object OnBackClick : ChatsUiEvent()
    data object OnAddOrCreateContactClick : ChatsUiEvent()
    data object OnSelectParticipantsForGroup : ChatsUiEvent()
    data class OnAddParticipantClick(val participant: ChatGroupParticipants) : ChatsUiEvent()
    data class OnRemoveAddedParticipantClick(val participant: ChatGroupParticipants) : ChatsUiEvent()
    data object OnCreateGroupClick : ChatsUiEvent()
    data object OnAddFriendClick : ChatsUiEvent()
    data object OnSubmitContactClick : ChatsUiEvent()
    data class OnGroupImagePicked(val uri: Uri) : ChatsUiEvent()
}

sealed class NavigationState {
    data object None : NavigationState()
    data class ChatBox(val chatGroup: ChatGroup) : NavigationState()
    data object Settings : NavigationState()
}

sealed class ScreenState {
    data object Chats : ScreenState()
    data object CreateContact : ScreenState()
    data object CreateChatGroup : ScreenState()
    data object SelectParticipantsForGroup : ScreenState()
    data object AddFriend : ScreenState()
}