package com.ahmetocak.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.domain.usecase.friend.CreateFriendUseCase
import com.ahmetocak.domain.usecase.friend.ObserveFriendsUseCase
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
class ChatsViewModel @Inject constructor(
    private val observeUserInCacheUseCase: ObserveUserInCacheUseCase,
    private val createFriendUseCase: CreateFriendUseCase,
    private val observeFriendsUseCase: ObserveFriendsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatsUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private lateinit var currentUser: User

    init {
        observeUser()
        observeFriends()
    }

    fun onEvent(event: ChatsUiEvent) {
        when (event) {
            is ChatsUiEvent.OnLoadingStateChange -> _uiState.update { it.copy(loadingState = event.state) }
            is ChatsUiEvent.OnChatItemClick -> _navigationState.update {
                NavigationState.ChatBox(event.id)
            }

            is ChatsUiEvent.OnCreateContactClick -> _uiState.update {
                it.copy(screenState = ScreenState.CreateChatRoomOrAddPerson)
            }

            is ChatsUiEvent.OpenAddPersonScreenClick -> _uiState.update {
                it.copy(screenState = ScreenState.AddPerson)
            }

            is ChatsUiEvent.OnBackClick -> {
                when (_uiState.value.screenState) {
                    is ScreenState.CreateChatRoomOrAddPerson -> _uiState.update {
                        it.copy(screenState = ScreenState.Chats)
                    }

                    is ScreenState.AddPerson -> _uiState.update {
                        it.copy(
                            screenState = ScreenState.CreateChatRoomOrAddPerson,
                            personValue = ""
                        )
                    }

                    // There is no back button in chats screen
                    else -> {}
                }
            }

            is ChatsUiEvent.OnPersonValueChanged -> _uiState.update { it.copy(personValue = event.value) }
            is ChatsUiEvent.AddNewPersonClick -> addNewPerson()
        }
    }

    private fun addNewPerson() {
        viewModelScope.launch(dispatcher) {
            _uiState.update { it.copy(addNewPersonLoadingState = LoadingState.Loading) }

            createFriendUseCase(
                userEmail = currentUser.email,
                friendEmail = _uiState.value.personValue,
                onSuccess = {
                    _uiState.update { it.copy(addNewPersonLoadingState = LoadingState.Idle) }
                },
                onFailure = { errorMessage ->
                    _uiState.update {
                        it.copy(addNewPersonLoadingState = LoadingState.Idle)
                    }
                    SnackbarManager.showMessage(errorMessage)
                }
            )
        }
    }

    private fun observeFriends() {
        viewModelScope.launch(dispatcher) {
            when (val response = observeFriendsUseCase()) {
                is Response.Success -> {
                    response.data.collect { friendList ->
                        _uiState.update {
                            it.copy(friendList = friendList)
                        }
                    }
                }

                is Response.Error -> SnackbarManager.showMessage(response.errorMessage)
            }
        }
    }

    private fun observeUser() {
        viewModelScope.launch(dispatcher) {
            when (val response = observeUserInCacheUseCase()) {
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

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}