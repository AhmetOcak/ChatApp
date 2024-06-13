package com.ahmetocak.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.domain.usecase.user.UpdateUserUseCase
import com.ahmetocak.domain.usecase.user.local.ObserveUserInCacheUseCase
import com.ahmetocak.model.LoadingState
import com.ahmetocak.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val observeUserInCacheUseCase: ObserveUserInCacheUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    init {
        observeUser()
    }

    private lateinit var currentUser: User

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.OnUpdateUserNameClick -> updateUser()
            is ProfileUiEvent.OnShowUpdateUsernameSheet -> _uiState.update {
                it.copy(showUpdateUserNameSheet = true)
            }

            is ProfileUiEvent.OnDismissUpdateUsernameSheet -> _uiState.update {
                it.copy(showUpdateUserNameSheet = false, value = "")
            }

            is ProfileUiEvent.OnUploadImageClick -> updateUser(imageUri = event.uri.toString())
            is ProfileUiEvent.OnValueChange -> _uiState.update { it.copy(value = event.value) }
            is ProfileUiEvent.OnBackClick -> _navigationState.update { NavigationState.Back }
        }
    }

    private fun observeUser() {
        viewModelScope.launch(dispatcher) {
            when (val response = observeUserInCacheUseCase()) {
                is Response.Success -> {
                    response.data.collect { user ->
                        if (user != null) {
                            _uiState.update {
                                it.copy(
                                    username = user.username,
                                    userEmail = user.email,
                                    userImageUrl = user.profilePicUrl
                                )
                            }
                            currentUser = user
                        }
                    }
                }

                is Response.Error -> SnackbarManager.showMessage(response.errorMessage)
            }
        }
    }

    private fun updateUser(imageUri: String? = null) {
        _uiState.update {
            it.copy(
                loadingState = LoadingState.Loading,
                imageUploadingState = if (imageUri != null) LoadingState.Loading else LoadingState.Idle
            )
        }

        viewModelScope.launch(dispatcher) {
            updateUserUseCase(
                imageUri = imageUri,
                user = currentUser.copy(
                    username = _uiState.value.value.ifBlank { currentUser.username }
                ),
                onFailure = { errorMessage ->
                    _uiState.update {
                        it.copy(
                            loadingState = LoadingState.Idle,
                            imageUploadingState = LoadingState.Idle
                        )
                    }
                    SnackbarManager.showMessage(errorMessage)
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            loadingState = LoadingState.Idle,
                            imageUploadingState = LoadingState.Idle,
                            value = "",
                            showUpdateUserNameSheet = false
                        )
                    }
                }
            )
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}