package com.ahmetocak.user_detail

import com.ahmetocak.model.LoadingState

data class UserDetailUiState(
    val userImage: String? = null,
    val username: String = "",
    val userEmail: String = "",
    val loadingState: LoadingState = LoadingState.Loading
)

sealed class UserDetailUiEvent {
    data object OnImageClick : UserDetailUiEvent()
    data object OnBackClick : UserDetailUiEvent()
    data object OnVoiceCallClick : UserDetailUiEvent()
    data object OnVideoCallClick : UserDetailUiEvent()
}

enum class NavigationState {
    None,
    Back
}