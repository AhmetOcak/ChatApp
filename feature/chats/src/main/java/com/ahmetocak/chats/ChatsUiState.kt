package com.ahmetocak.chats

import com.ahmetocak.model.Friend
import com.ahmetocak.model.LoadingState

data class ChatsUiState(
    val personValue: String = "",
    val friendList: List<Friend> = emptyList(),
    val screenState: ScreenState = ScreenState.Chats,
    val loadingState: LoadingState = LoadingState.Idle,
    val addNewPersonLoadingState: LoadingState = LoadingState.Idle
)

sealed class ChatsUiEvent {
    data class OnChatItemClick(
        val friendshipId: Int,
        val friendEmail: String?,
        val friendUsername: String?,
        val friendProfPicUrl: String?
    ) : ChatsUiEvent()

    data class OnLoadingStateChange(val state: LoadingState) : ChatsUiEvent()
    data object OnCreateContactClick : ChatsUiEvent()
    data object OpenAddPersonScreenClick : ChatsUiEvent()
    data object OnBackClick : ChatsUiEvent()
    data class OnPersonValueChanged(val value: String) : ChatsUiEvent()
    data object AddNewPersonClick : ChatsUiEvent()
    data object OnSettingsClick : ChatsUiEvent()
}

sealed class NavigationState {
    data object None : NavigationState()
    data class ChatBox(
        val friendshipId: Int,
        val friendEmail: String?,
        val friendUsername: String?,
        val friendProfPicUrl: String?
    ) : NavigationState()
    data object Settings : NavigationState()
}

sealed class ScreenState {
    data object Chats : ScreenState()
    data object CreateChatRoomOrAddPerson : ScreenState()
    data object AddPerson : ScreenState()
}