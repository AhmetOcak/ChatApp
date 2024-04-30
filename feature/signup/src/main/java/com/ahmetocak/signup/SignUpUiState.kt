package com.ahmetocak.signup

import android.content.Context
import com.ahmetocak.model.LoadingState

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val loadingState: LoadingState = LoadingState.Idle
)

sealed class SignUpEvent {
    data class OnNameChanged(val value: String) : SignUpEvent()
    data class OnEmailChanged(val value: String) : SignUpEvent()
    data class OnPasswordChange(val value: String) : SignUpEvent()
    data class OnConfirmPasswordChange(val value: String) : SignUpEvent()
    data class OnLoadingStateChange(val state: LoadingState) : SignUpEvent()
    data class OnGoogleClicked(val context: Context) : SignUpEvent()
    data object OnNavigateUpClicked : SignUpEvent()
    data object OnSignUpClick : SignUpEvent()
    data object OnAlreadyHaveAccountClick : SignUpEvent()
}

enum class NavigationState {
    None,
    NavigateUp,
    Login,
    Chats
}