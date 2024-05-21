package com.ahmetocak.profile

import android.net.Uri
import com.ahmetocak.model.LoadingState

data class ProfileUiState(
    val userImageUrl: String? = null,
    val username: String = "",
    val value: String = "",
    val userEmail: String = "",
    val showUpdateUserNameSheet: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val loadingState: LoadingState = LoadingState.Idle,
    val imageUploadingState: LoadingState = LoadingState.Idle,
    val deleteAccountState: LoadingState = LoadingState.Idle
)

sealed class ProfileUiEvent {
    data class OnValueChange(val value: String) : ProfileUiEvent()
    data object OnImageClick : ProfileUiEvent()
    data class OnUploadImageClick(val uri: Uri) : ProfileUiEvent()
    data object OnStartDeleteAccountDialogClick : ProfileUiEvent()
    data object OnDismissDeleteAccountDialog : ProfileUiEvent()
    data object OnDeleteAccountClick : ProfileUiEvent()
    data object OnShowUpdateUsernameSheet : ProfileUiEvent()
    data object OnDismissUpdateUsernameSheet : ProfileUiEvent()
    data object OnUpdateUserNameClick : ProfileUiEvent()
    data object OnBackClick : ProfileUiEvent()
    data object OnLogOutClick : ProfileUiEvent()
}

enum class NavigationState {
    None,
    Back,
    Login
}