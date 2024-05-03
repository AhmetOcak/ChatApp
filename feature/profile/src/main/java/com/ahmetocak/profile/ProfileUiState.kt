package com.ahmetocak.profile

import android.net.Uri

data class ProfileUiState(
    val userImageUrl: String? = null,
    val username: String = "",
    val value: String = "",
    val userEmail: String = ""
)

sealed class ProfileUiEvent {
    data class OnValueChange(val value: String) : ProfileUiEvent()
    data object OnImageClick : ProfileUiEvent()
    data class OnUploadImageClick(val uri: Uri) : ProfileUiEvent()
    data object OnUpdateUserNameClick : ProfileUiEvent()
    data object OnBackClick : ProfileUiEvent()
}

enum class NavigationState {
    None,
    Back
}