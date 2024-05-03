package com.ahmetocak.user_detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(UserDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    fun onEvent(event: UserDetailUiEvent) {
        when (event) {
            is UserDetailUiEvent.OnImageClick -> { /* TODO: Scale the image */ }
            is UserDetailUiEvent.OnVideoCallClick -> { /* TODO: Start a video call */ }
            is UserDetailUiEvent.OnVoiceCallClick -> { /* TODO: Start a voice call */ }
            is UserDetailUiEvent.OnBackClick -> _navigationState.update { NavigationState.Back }
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}