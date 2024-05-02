package com.ahmetocak.calls

import com.ahmetocak.model.Call
import com.ahmetocak.model.LoadingState

data class CallsUiState(
    val callList: List<Call> = emptyList(),
    val showCallListToolbar: Boolean = false,
    val callImgState: CallsImageClickState = CallsImageClickState.Idle,
    val loadingState: LoadingState = LoadingState.Loading
)

sealed class CallsUiEvent {
    data class OnCallClick(val id: String) : CallsUiEvent()
    data class OnCallImageClick(val id: String) : CallsUiEvent()
    data class OnCallLongClick(val id: String) : CallsUiEvent()
    data class OnLoadingStateChanged(val state: LoadingState) : CallsUiEvent()
}

sealed class NavigationState {
    data object None : NavigationState()
    data class CallDetail(val id: String): NavigationState()
}

sealed class CallsImageClickState {
    data object Idle : CallsImageClickState()
    data object ShowMiniChatMenu : CallsImageClickState()
    data object ShowFullScreenImage : CallsImageClickState()
}