package com.ahmetocak.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.domain.usecase.app_preferences.GetAppThemeUseCase
import com.ahmetocak.domain.usecase.app_preferences.GetDynamicColorUseCase
import com.ahmetocak.domain.usecase.app_preferences.UpdateDarkModeUseCase
import com.ahmetocak.domain.usecase.app_preferences.UpdateDynamicColorUseCase
import com.ahmetocak.domain.usecase.auth.SignOutUseCase
import com.ahmetocak.domain.usecase.user.DeleteUserUseCase
import com.ahmetocak.domain.usecase.user.local.ObserveUserInCacheUseCase
import com.ahmetocak.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val deleteUserUseCase: DeleteUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val observeUserUseCase: ObserveUserInCacheUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val getDynamicColorUseCase: GetDynamicColorUseCase,
    private val updateDarkModeUseCase: UpdateDarkModeUseCase,
    private val updateDynamicColorUseCase: UpdateDynamicColorUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private var currentUser: User? = null

    init {
        observeAppTheme()
        observeDynamicColor()
        observeUser()
    }

    fun onEvent(event: SettingUiEvent) {
        when (event) {
            is SettingUiEvent.OnDarkModeSwitched -> updateDarkMode(event.value)
            is SettingUiEvent.OnDynamicColorSwitched -> updateDynamicColor(event.value)
            is SettingUiEvent.OnSignOutClick -> {
                signOutUseCase()
                _navigationState.update { NavigationState.Login }
            }

            is SettingUiEvent.OnStartDeleteAccountDialogClick -> _uiState.update {
                it.copy(showDeleteAccountDialog = true)
            }

            is SettingUiEvent.OnDismissDeleteAccountDialog -> _uiState.update {
                it.copy(showDeleteAccountDialog = false)
            }

            is SettingUiEvent.OnSubmitDeleteAccountClick -> deleteAccount()
            is SettingUiEvent.OnPasswordValueChange -> _uiState.update {
                it.copy(password = event.value)
            }
        }
    }

    private fun deleteAccount() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(ioDispatcher) {
            if (currentUser != null) {
                deleteUserUseCase(
                    user = currentUser!!,
                    password = _uiState.value.password,
                    onSuccess = {
                        signOutUseCase()
                        _navigationState.update { NavigationState.Login }
                    },
                    onFailure = { errorMessage ->
                        SnackbarManager.showMessage(errorMessage)
                        _uiState.update { it.copy(isLoading = false) }
                    }
                )
            }
        }
    }

    private fun observeUser() {
        viewModelScope.launch(ioDispatcher) {
            when (val response = observeUserUseCase()) {
                is Response.Success -> {
                    response.data.collect { user ->
                        if (user != null) {
                            currentUser = user
                        }
                    }
                }

                is Response.Error -> SnackbarManager.showMessage(response.errorMessage)
            }
        }
    }

    private fun observeAppTheme() {
        viewModelScope.launch(ioDispatcher) {
            getAppThemeUseCase().collect { isDarkMode ->
                _uiState.update { it.copy(isDarkMode = isDarkMode) }
            }
        }
    }

    private fun observeDynamicColor() {
        viewModelScope.launch(ioDispatcher) {
            getDynamicColorUseCase().collect { isDynamicColor ->
                _uiState.update { it.copy(isDynamicColor = isDynamicColor) }
            }
        }
    }

    private fun updateDarkMode(value: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            updateDarkModeUseCase(value)
        }
    }

    private fun updateDynamicColor(value: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            updateDynamicColorUseCase(value)
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}