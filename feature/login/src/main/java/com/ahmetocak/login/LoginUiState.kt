package com.ahmetocak.login

import android.content.Context
import com.ahmetocak.model.DialogState
import com.ahmetocak.model.LoadingState

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val dialogState: DialogState = DialogState.Hide,
    val loadingState: LoadingState = LoadingState.Idle
)

sealed class LoginEvent {
    data class OnEmailChanged(val email: String) : LoginEvent()
    data class OnPasswordChanged(val password: String) : LoginEvent()
    data class OnLoadingStateChanged(val state: LoadingState) : LoginEvent()
    data class OnGoogleClicked(val context: Context) : LoginEvent()
    data object OnLoginClicked : LoginEvent()
    data class OnForgotPasswordClick(val state: DialogState) : LoginEvent()
    data object OnSignUpClicked : LoginEvent()
}

enum class NavigationState {
    None,
    Chats,
    SignUp
}