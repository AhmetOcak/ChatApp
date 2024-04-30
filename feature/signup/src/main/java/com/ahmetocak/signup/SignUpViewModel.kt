package com.ahmetocak.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow(NavigationState.None)
    val navigationState get() = _navigationState.asStateFlow()

    private val name get() = _uiState.value.name
    private val email get() = _uiState.value.email
    private val password get() = _uiState.value.password
    private val confirmPassword get() = _uiState.value.confirmPassword

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnNameChanged -> _uiState.update { it.copy(name = event.value) }
            is SignUpEvent.OnEmailChanged -> _uiState.update { it.copy(email = event.value) }
            is SignUpEvent.OnPasswordChange -> _uiState.update { it.copy(password = event.value) }
            is SignUpEvent.OnConfirmPasswordChange -> _uiState.update { it.copy(confirmPassword = event.value) }
            is SignUpEvent.OnLoadingStateChange -> _uiState.update { it.copy(loadingState = event.state) }
            is SignUpEvent.OnNavigateUpClicked -> _navigationState.update { NavigationState.NavigateUp }
            is SignUpEvent.OnSignUpClick -> signUp()
            is SignUpEvent.OnGoogleClicked -> googleSignIn(event.context)
            is SignUpEvent.OnAlreadyHaveAccountClick -> _navigationState.update { NavigationState.Login }
        }
    }

    private fun signUp() {

    }

    private fun googleSignIn(context: Context) {

    }

    fun resetNavigationState() {
        _navigationState.update { NavigationState.None }
    }
}