package com.ahmetocak.chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.ext.encodeForSaveNav
import com.ahmetocak.common.websocket.Connection
import com.ahmetocak.common.websocket.WebSocketListener
import com.ahmetocak.common.websocket.WebSocketManager
import com.ahmetocak.domain.usecase.friend.CreateFriendUseCase
import com.ahmetocak.domain.usecase.friend.ObserveFriendsUseCase
import com.ahmetocak.domain.usecase.user.UploadUserFcmTokenUseCase
import com.ahmetocak.domain.usecase.user.local.ObserveUserInCacheUseCase
import com.ahmetocak.model.LoadingState
import com.ahmetocak.model.User
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val observeUserInCacheUseCase: ObserveUserInCacheUseCase,
    private val createFriendUseCase: CreateFriendUseCase,
    private val observeFriendsUseCase: ObserveFriendsUseCase,
    private val uploadUserFcmTokenUseCase: UploadUserFcmTokenUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatsUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private lateinit var currentUser: User

    init {
        observeUser()
        observeFriends()
        observeWebSocketConnection()
    }

    fun onEvent(event: ChatsUiEvent) {
        when (event) {
            is ChatsUiEvent.OnChatItemClick -> _navigationState.update {
                NavigationState.ChatBox(
                    event.friendshipId,
                    event.friendEmail,
                    event.friendUsername,
                    event.friendProfPicUrl.encodeForSaveNav()
                )
            }

            is ChatsUiEvent.OnFriendValueChanged -> _uiState.update { it.copy(friendValue = event.value) }
            is ChatsUiEvent.OnSettingsClick -> _navigationState.update { NavigationState.Settings }
            is ChatsUiEvent.OnShowAddFriendSheetClick -> _uiState.update {
                it.copy(showAddFriendBottomSheet = true)
            }

            is ChatsUiEvent.OnDismissAddFriendSheet -> _uiState.update {
                it.copy(showAddFriendBottomSheet = false, friendValue = "")
            }

            is ChatsUiEvent.OnAddFriendClick -> addFriend()
        }
    }

    private fun addFriend() {
        viewModelScope.launch(dispatcher) {
            _uiState.update {
                it.copy(addFriendLoadingState = LoadingState.Loading, addFriendErrorMessage = null)
            }

            createFriendUseCase(
                userEmail = currentUser.email,
                friendEmail = _uiState.value.friendValue,
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            addFriendErrorMessage = null,
                            addFriendLoadingState = LoadingState.Idle,
                            friendValue = "",
                            showAddFriendBottomSheet = false
                        )
                    }
                },
                onFailure = { errorMessage ->
                    _uiState.update {
                        it.copy(
                            addFriendErrorMessage = errorMessage,
                            addFriendLoadingState = LoadingState.Idle
                        )
                    }
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
                            uploadFcmToken(user.email)
                        }
                    }
                }

                is Response.Error -> SnackbarManager.showMessage(response.errorMessage)
            }
        }
    }

    private fun observeWebSocketConnection() {
        viewModelScope.launch(dispatcher) {
            WebSocketListener.isConnected.collect { connection ->
                when (connection) {
                    Connection.CONNECTED -> Log.d("WEB SOCKET OBSERVER", connection.name)
                    Connection.NOT_CONNECTED -> {
                        while (true) {
                            if (this@ChatsViewModel::currentUser.isInitialized) {
                                WebSocketManager.initializeWebsocket(currentUser.email)
                            }
                            Log.d("WEB SOCKET OBSERVER", "reconnecting")
                            delay(10000)
                            if (WebSocketListener.isConnected.value == Connection.CONNECTED) {
                                Log.d("isConnected", WebSocketListener.isConnected.value.name)
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private fun uploadFcmToken(email: String) {
        viewModelScope.launch(dispatcher) {
            uploadUserFcmTokenUseCase(
                email = email,
                token = FirebaseMessaging.getInstance().token.await(),
                onFailure = SnackbarManager::showMessage
            )
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}