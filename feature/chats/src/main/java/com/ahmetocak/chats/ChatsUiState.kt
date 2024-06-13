package com.ahmetocak.chats

import com.ahmetocak.common.UiText
import com.ahmetocak.model.Friend
import com.ahmetocak.model.LoadingState

data class ChatsUiState(
    val friendValue: String = "",
    val friendList: List<Friend> = emptyList(),
    val showAddFriendBottomSheet: Boolean = false,
    val addFriendErrorMessage: UiText? = null,
    val addFriendLoadingState: LoadingState = LoadingState.Idle
)

sealed class ChatsUiEvent {
    data class OnChatItemClick(
        val friendshipId: Int,
        val friendEmail: String?,
        val friendUsername: String?,
        val friendProfPicUrl: String?
    ) : ChatsUiEvent()

    data class OnFriendValueChanged(val value: String) : ChatsUiEvent()
    data object OnShowAddFriendSheetClick : ChatsUiEvent()
    data object OnDismissAddFriendSheet : ChatsUiEvent()
    data object OnAddFriendClick : ChatsUiEvent()
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