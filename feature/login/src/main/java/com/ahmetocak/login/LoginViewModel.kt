package com.ahmetocak.login

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow(NavigationState.None)
    val navigationState get() = _navigationState.asStateFlow()

    private val email get() = _uiState.value.email
    private val password get() = _uiState.value.password

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChanged -> _uiState.update { it.copy(email = event.email) }
            is LoginEvent.OnPasswordChanged -> _uiState.update { it.copy(password = event.password) }
            is LoginEvent.OnGoogleClicked -> googleSignIn(event.context)
            is LoginEvent.OnForgotPasswordClick -> _uiState.update { it.copy(dialogState = event.state) }
            is LoginEvent.OnLoadingStateChanged -> _uiState.update { it.copy(loadingState = event.state) }
            is LoginEvent.OnLoginClicked -> onLogin()
            is LoginEvent.OnSignUpClicked -> _navigationState.update { NavigationState.SignUp }
        }
    }

    private fun googleSignIn(context: Context) {

    }

    private fun onLogin() {

    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}