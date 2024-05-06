package com.ahmetocak.login

import androidx.activity.result.IntentSenderRequest
import com.ahmetocak.model.LoadingState

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val resetEmail: String = "",
    val showForgotPasswordDialog: Boolean = false,
    val loadingState: LoadingState = LoadingState.Idle
)

sealed class LoginUiEvent {
    data class OnEmailChanged(val email: String) : LoginUiEvent()
    data class OnPasswordChanged(val password: String) : LoginUiEvent()
    data class OnResetEmailChanged(val resetEmail: String) : LoginUiEvent()
    data class OnLoadingStateChanged(val state: LoadingState) : LoginUiEvent()
    data class OnGoogleClicked(val result: (IntentSenderRequest) -> Unit) : LoginUiEvent()
    data object OnLoginClickedUi : LoginUiEvent()
    data object OnForgotPasswordClick : LoginUiEvent()
    data object OnSignUpClicked : LoginUiEvent()
    data object OnSendPasswordResetMailClick : LoginUiEvent()
}

enum class NavigationState {
    None,
    Chats,
    SignUp
}