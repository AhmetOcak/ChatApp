package com.ahmetocak.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private val username get() = _uiState.value.username

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnImageClick -> { /* TODO: Show image in full screen */ }
            is ProfileUiEvent.OnUpdateUserNameClick -> { /* TODO: Update user name */ }
            is ProfileUiEvent.OnUploadImageClick -> { /* TODO: Upload image */ }
            is ProfileUiEvent.OnValueChange -> { _uiState.update { it.copy(value = event.value) } }
            is ProfileUiEvent.OnBackClick -> { _navigationState.update { NavigationState.Back } }
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}