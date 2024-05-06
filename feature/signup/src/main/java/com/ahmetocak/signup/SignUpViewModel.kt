package com.ahmetocak.signup

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.confirmPassword
import com.ahmetocak.common.ext.isValidEmail
import com.ahmetocak.common.ext.isValidName
import com.ahmetocak.common.ext.isValidPassword
import com.ahmetocak.domain.usecase.auth.SignInWithGoogleUseCase
import com.ahmetocak.domain.usecase.auth.SignUpUseCase
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
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

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
            SignUpEvent.OnNavigateUpClicked -> _navigationState.update { NavigationState.NavigateUp }
            SignUpEvent.OnSignUpClick -> signUp()
            is SignUpEvent.OnGoogleClicked -> startGoogleSignInIntent(event.result)
            SignUpEvent.OnAlreadyHaveAccountClick -> _navigationState.update { NavigationState.Login }
        }
    }

    private fun signUp() {
        if (formValidation()) {
            _uiState.update { it.copy(loadingState = LoadingState.Loading) }

            viewModelScope.launch(ioDispatcher) {
                signUpUseCase(
                    name = name,
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

    private fun formValidation(): Boolean {
        return if (!email.isValidEmail()) {
            SnackbarManager.showMessage(UiText.StringResource(AppStrings.email_error))
            false
        } else if (!password.isValidPassword()) {
            SnackbarManager.showMessage(UiText.StringResource(AppStrings.password_error))
            false
        } else if (!name.isValidName()) {
            SnackbarManager.showMessage(UiText.StringResource(AppStrings.name_error))
            false
        } else if (!password.confirmPassword(confirmPassword)) {
            SnackbarManager.showMessage(UiText.StringResource(AppStrings.confirm_password_error))
            false
        } else true
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

    fun resetNavigationState() {
        _navigationState.update { NavigationState.None }
    }
}