package com.ahmetocak.chats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.chats.components.ChatsScreenTopBar
import com.ahmetocak.chats.components.CreateContentItem
import com.ahmetocak.designsystem.components.ChatAppOutlinedTextField
import com.ahmetocak.designsystem.components.ChatAppProgressIndicator
import com.ahmetocak.designsystem.components.ChatAppScaffold
import com.ahmetocak.designsystem.components.ChatButton
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.Friend
import com.ahmetocak.model.LoadingState
import com.ahmetocak.ui.ChatItem

@Composable
internal fun ChatsRoute(
    onNavigateToChatBox: (String, String, String?) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateSettings: () -> Unit,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: ChatsUiEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(onAction: () -> Unit) {
            onAction()
            viewModel.resetNavigation()
        }

        when (val state = navigationState) {
            is NavigationState.ChatBox -> performNavigation {
                onNavigateToChatBox(
                    state.friendEmail,
                    state.friendUsername,
                    state.friendProfPicUrl
                )
            }

            is NavigationState.Settings -> performNavigation { onNavigateSettings() }
            is NavigationState.None -> Unit
        }
    }

    ChatAppProgressIndicator(visible = uiState.loadingState is LoadingState.Loading)

    ChatAppScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ChatsScreenTopBar(
                screenState = uiState.screenState,
                onEvent = onEvent
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = uiState.screenState is ScreenState.Chats,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(onClick = { onEvent(ChatsUiEvent.OnCreateContactClick) }) {
                    Icon(
                        imageVector = ChatAppIcons.Filled.add,
                        contentDescription = null
                    )
                }
            }
        }
    ) { paddingValues ->
        when (uiState.screenState) {
            ScreenState.Chats -> {
                ChatsScreen(
                    modifier = Modifier.padding(paddingValues),
                    friendList = uiState.friendList,
                    onEvent = onEvent
                )
            }

            ScreenState.CreateChatRoomOrAddPerson -> {
                CreateChatRoomOrAddPersonScreen(
                    modifier = Modifier.padding(paddingValues),
                    onEvent = onEvent
                )
            }

            ScreenState.AddPerson -> {
                AddNewPersonScreen(
                    onEvent = onEvent,
                    newPersonValue = uiState.personValue,
                    isLoading = uiState.addNewPersonLoadingState == LoadingState.Loading
                )
            }
        }
    }
}


@Composable
private fun ChatsScreen(
    modifier: Modifier = Modifier,
    friendList: List<Friend>,
    onEvent: (ChatsUiEvent) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(friendList, key = { it.id }) { chat ->
            ChatItem(
                id = chat.id,
                title = chat.friendEmail,
                imageUrl = chat.friendProfilePicUrl,
                onClick = {
                    onEvent(
                        ChatsUiEvent.OnChatItemClick(
                            chat.friendEmail,
                            chat.friendUsername,
                            chat.friendProfilePicUrl
                        )
                    )
                },
            )
        }
    }
}

@Composable
internal fun CreateChatRoomOrAddPersonScreen(
    modifier: Modifier = Modifier,
    onEvent: (ChatsUiEvent) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        CreateContentItem(
            title = "New Person",
            onClick = { onEvent(ChatsUiEvent.OpenAddPersonScreenClick) }
        )
    }
}

@Composable
private fun AddNewPersonScreen(
    modifier: Modifier = Modifier,
    onEvent: (ChatsUiEvent) -> Unit,
    newPersonValue: String,
    isLoading: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        ChatAppOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = newPersonValue,
            onValueChange = { onEvent(ChatsUiEvent.OnPersonValueChanged(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = {
                Text(text = "Person Email")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ChatButton(
            onClick = { onEvent(ChatsUiEvent.AddNewPersonClick) },
            enabled = newPersonValue.isNotBlank() && !isLoading
        ) {
            Text(text = "Add Person")
        }
    }
}

@Preview
@Composable
fun ChatsScreenPreview() {
    ChatAppTheme {
        Surface {
            ChatsScreen(
                friendList = emptyList(),
                onEvent = {}
            )
        }
    }
}