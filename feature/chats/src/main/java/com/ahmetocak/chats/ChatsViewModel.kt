package com.ahmetocak.chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.UiText
import com.ahmetocak.common.websocket.Connection
import com.ahmetocak.common.websocket.WebSocketListener
import com.ahmetocak.common.websocket.WebSocketManager
import com.ahmetocak.domain.usecase.chat_group.CreateChatGroupUseCase
import com.ahmetocak.domain.usecase.chat_group.CreatePrivateGroupUseCase
import com.ahmetocak.domain.usecase.chat_group.GetChatGroupsAndCacheUseCase
import com.ahmetocak.domain.usecase.chat_group.ObserveChatGroupsInCache
import com.ahmetocak.domain.usecase.user.UploadUserFcmTokenUseCase
import com.ahmetocak.domain.usecase.user.local.ObserveUserInCacheUseCase
import com.ahmetocak.model.ChatGroupParticipants
import com.ahmetocak.model.GroupType
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
    private val uploadUserFcmTokenUseCase: UploadUserFcmTokenUseCase,
    private val createChatGroupUseCase: CreateChatGroupUseCase,
    private val createPrivateGroupUseCase: CreatePrivateGroupUseCase,
    private val observeChatGroupsInCache: ObserveChatGroupsInCache,
    private val getChatGroupsAndCacheUseCase: GetChatGroupsAndCacheUseCase,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatsUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private lateinit var currentUser: User

    init {
        initializeUserData()
        observeWebSocketConnection()
    }

    fun onEvent(event: ChatsUiEvent) {
        when (event) {
            is ChatsUiEvent.OnAddOrCreateContactClick -> _uiState.update {
                it.copy(screenState = ScreenState.CreateContact)
            }

            is ChatsUiEvent.OnAddFriendClick -> _uiState.update {
                it.copy(screenState = ScreenState.AddFriend)
            }

            is ChatsUiEvent.OnSelectParticipantsForGroup -> _uiState.update {
                it.copy(screenState = ScreenState.SelectParticipantsForGroup)
            }

            is ChatsUiEvent.OnCreateGroupClick -> {
                if (uiState.value.selectedParticipants.isNotEmpty()) {
                    _uiState.update {
                        it.copy(screenState = ScreenState.CreateChatGroup)
                    }
                } else {
                    SnackbarManager.showMessage(
                        message = UiText.DynamicString("You must select participants.")
                    )
                }
            }

            is ChatsUiEvent.OnBackClick -> handleBackClick()
            is ChatsUiEvent.OnSubmitContactClick -> createContact()
            is ChatsUiEvent.OnChatItemClick -> _navigationState.update {
                NavigationState.ChatBox(event.chatGroup)
            }

            is ChatsUiEvent.OnValueChanged -> _uiState.update { it.copy(textFieldValue = event.value) }
            is ChatsUiEvent.OnSearchValueChanged -> _uiState.update { it.copy(searchValue = event.value) }
            is ChatsUiEvent.OnSettingsClick -> _navigationState.update { NavigationState.Settings }

            is ChatsUiEvent.OnAddParticipantClick -> {
                val updatedList = uiState.value.selectedParticipants.toMutableList()
                updatedList.add(event.participant)
                _uiState.update {
                    it.copy(selectedParticipants = updatedList)
                }
            }

            is ChatsUiEvent.OnRemoveAddedParticipantClick -> {
                val updatedList = uiState.value.selectedParticipants.toMutableList()
                updatedList.remove(event.participant)
                _uiState.update {
                    it.copy(selectedParticipants = updatedList)
                }
            }

            is ChatsUiEvent.OnGroupImagePicked -> {
                _uiState.update {
                    it.copy(selectedGroupImgUri = event.uri)
                }
            }
        }
    }

    private fun createContact() {
        viewModelScope.launch(dispatcher) {
            _uiState.update {
                it.copy(loadingState = LoadingState.Loading)
            }

            if (uiState.value.screenState is ScreenState.AddFriend) {
                when (val response = createPrivateGroupUseCase(
                    currentUser.email,
                    friendEmail = uiState.value.textFieldValue
                )) {
                    is Response.Success -> {
                        _uiState.update {
                            it.copy(loadingState = LoadingState.Idle)
                        }
                    }

                    is Response.Error -> {
                        _uiState.update {
                            it.copy(loadingState = LoadingState.Idle)
                        }
                        Log.e("addFriend", response.errorMessage.toString())
                    }
                }
            } else {
                if (uiState.value.textFieldValue.isNotBlank()) {
                    when (val response = createChatGroupUseCase(
                        creatorEmail = currentUser.email,
                        groupName = uiState.value.textFieldValue,
                        creatorUsername = currentUser.username,
                        creatorProfilePicUrl = currentUser.profilePicUrl,
                        groupImageUrl = uiState.value.selectedGroupImgUri
                    )) {
                        is Response.Success -> {
                            _uiState.update {
                                it.copy(loadingState = LoadingState.Idle)
                            }
                        }

                        is Response.Error -> {
                            _uiState.update {
                                it.copy(loadingState = LoadingState.Idle)
                            }
                            Log.e("addFriend", response.errorMessage.toString())
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(loadingState = LoadingState.Idle)
                    }
                    SnackbarManager.showMessage(
                        UiText.DynamicString("Please enter the group name")
                    )
                }
            }
        }
    }

    private fun getFriends(userEmail: String) {
        viewModelScope.launch(dispatcher) {
            getChatGroupsAndCacheUseCase(
                userEmail = userEmail,
                onComplete = {
                    observeChatGroupsInCache().collect { chatList ->
                        val privateChatGroups = chatList.filter {
                            it.groupType == GroupType.PRIVATE_CHAT_GROUP
                        }
                        val addableParticipants: MutableList<ChatGroupParticipants> = mutableListOf()
                        privateChatGroups.forEach {
                            it.participants.forEach { participant ->
                                if (participant.participantEmail != currentUser.email) {
                                    addableParticipants.add(
                                        ChatGroupParticipants(
                                            id = participant.id,
                                            participantEmail = participant.participantEmail,
                                            participantUsername = participant.participantUsername,
                                            participantProfilePicUrl = participant.participantProfilePicUrl
                                        )
                                    )
                                }
                            }
                        }
                        _uiState.update { state ->
                            state.copy(chatList = chatList, participantList = addableParticipants)
                        }
                    }
                }
            )
        }
    }

    private fun initializeUserData() {
        viewModelScope.launch(dispatcher) {
            when (val response = observeUserInCacheUseCase()) {
                is Response.Success -> {
                    response.data.collect { user ->
                        if (user != null) {
                            currentUser = user
                            uploadFcmToken(user.email)
                            getFriends(user.email)
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

    private fun handleBackClick() {
        when (uiState.value.screenState) {
            is ScreenState.CreateChatGroup -> _uiState.update {
                it.copy(
                    screenState = ScreenState.SelectParticipantsForGroup,
                    textFieldValue = "",
                    selectedGroupImgUri = null
                )
            }

            is ScreenState.SelectParticipantsForGroup -> _uiState.update {
                it.copy(
                    screenState = ScreenState.CreateContact,
                    selectedParticipants = emptyList()
                )
            }

            is ScreenState.AddFriend -> _uiState.update {
                it.copy(screenState = ScreenState.CreateContact, textFieldValue = "")
            }

            is ScreenState.CreateContact -> _uiState.update {
                it.copy(screenState = ScreenState.Chats)
            }

            is ScreenState.Chats -> { /* There is no back action in chats screen */ }
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }
}