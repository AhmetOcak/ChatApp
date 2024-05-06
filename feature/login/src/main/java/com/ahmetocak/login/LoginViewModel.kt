package com.ahmetocak.login

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.isValidEmail
import com.ahmetocak.common.ext.isValidPassword
import com.ahmetocak.domain.usecase.auth.LoginUseCase
import com.ahmetocak.domain.usecase.auth.SendResetPasswordEmailUseCase
import com.ahmetocak.domain.usecase.auth.SignInWithGoogleUseCase
import com.ahmetocak.model.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ahmetocak.designsystem.R.string as AppStrings

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val sendResetPasswordEmailUseCase: SendResetPasswordEmailUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow(NavigationState.None)
    val navigationState get() = _navigationState.asStateFlow()

    private val email get() = _uiState.value.email
    private val resetEmail get() = _uiState.value.resetEmail
    private val password get() = _uiState.value.password

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnEmailChanged -> _uiState.update { it.copy(email = event.email) }
            is LoginUiEvent.OnPasswordChanged -> _uiState.update { it.copy(password = event.password) }
            is LoginUiEvent.OnResetEmailChanged -> _uiState.update { it.copy(resetEmail = event.resetEmail) }
            is LoginUiEvent.OnGoogleClicked -> startGoogleSignInIntent(event.result)
            LoginUiEvent.OnForgotPasswordClick -> _uiState.update { it.copy(showForgotPasswordDialog = true) }
            is LoginUiEvent.OnLoadingStateChanged -> _uiState.update { it.copy(loadingState = event.state) }
            LoginUiEvent.OnLoginClickedUi -> login()
            LoginUiEvent.OnSignUpClicked -> _navigationState.update { NavigationState.SignUp }
            LoginUiEvent.OnSendPasswordResetMailClick -> sendPasswordResetMail()
        }
    }

    private fun startGoogleSignInIntent(onSuccess: (IntentSenderRequest) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            signInWithGoogleUseCase.startSignInIntent(
                onSuccess = onSuccess,
                onFailure = { message ->
                    SnackbarManager.showMessage(message)
                }
            )
        }
    }

    fun googleSignIn(intent: Intent) {
        viewModelScope.launch(ioDispatcher) {
            signInWithGoogleUseCase.signInWithGoogle(
                intent,
                onSuccess = {
                    _navigationState.update { NavigationState.Chats }
                },
                onFailure = { message ->
                    SnackbarManager.showMessage(message)
                }
            )
        }
    }

    private fun login() {
        if (formValidation()) {
            _uiState.update { it.copy(loadingState = LoadingState.Loading) }

            viewModelScope.launch(ioDispatcher) {
                loginUseCase(
                    email = email,
                    password = password,
                    onSuccess = {
                        _uiState.update { it.copy(loadingState = LoadingState.Idle) }
                        _navigationState.update { NavigationState.Chats }
                    },
                    onFailure = { message ->
                        _uiState.update { it.copy(loadingState = LoadingState.Idle) }
                        SnackbarManager.showMessage(message)
                    }
                )
            }
        }
    }

    private fun sendPasswordResetMail() {
        if (resetEmail.isValidEmail()) {
            _uiState.update { it.copy(loadingState = LoadingState.Loading) }

            viewModelScope.launch(ioDispatcher) {
                sendResetPasswordEmailUseCase(
                    email = resetEmail,
                    onSuccess = {
                        SnackbarManager.showMessage(message = UiText.StringResource(AppStrings.password_reset_mail_sent))
                        _uiState.update { it.copy(loadingState = LoadingState.Idle) }
                    },
                    onFailure = { message ->
                        SnackbarManager.showMessage(message)
                        _uiState.update { it.copy(loadingState = LoadingState.Idle) }
                    }
                )
            }
        } else {
            _uiState.update { it.copy(loadingState = LoadingState.Idle) }
            SnackbarManager.showMessage(UiText.StringResource(AppStrings.email_error))
        }
    }

    private fun formValidation(): Boolean {
        return if (!email.isValidEmail()) {
            SnackbarManager.showMessage(UiText.StringResource(AppStrings.email_error))
            false
        } else if (!password.isValidPassword()) {
            SnackbarManager.showMessage(UiText.StringResource(AppStrings.password_error))
            false
        } else true
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }

    fun hideResetPasswordDialog() {
        _uiState.update {
            it.copy(showForgotPasswordDialog = false)
        }
    }
}