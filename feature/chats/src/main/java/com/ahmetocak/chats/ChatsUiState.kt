package com.ahmetocak.chats

import com.ahmetocak.model.LoadingState
import com.ahmetocak.model.UserChat

data class ChatsUiState(
    val chatList: List<UserChat> = emptyList(),
    val chatImageClickState: ChatImageClickState = ChatImageClickState.Idle,
    val loadingState: LoadingState = LoadingState.Loading
)

sealed class ChatsUiEvent {
    data class OnChatItemClick(val id: String): ChatsUiEvent()
    data class OnLoadingStateChange(val state: LoadingState): ChatsUiEvent()
    data object OnImageClick : ChatsUiEvent()
}

sealed class NavigationState {
    data object None : NavigationState()
    data class ChatBox(val id: String) : NavigationState()
}

sealed class ChatImageClickState {
    data object Idle : ChatImageClickState()
    data object ShowMiniChatMenu : ChatImageClickState()
    data object ShowFullScreenImage : ChatImageClickState()
}