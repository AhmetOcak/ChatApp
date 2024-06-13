package com.ahmetocak.chats

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.chats.components.ChatsScreenTopBar
import com.ahmetocak.common.UiText
import com.ahmetocak.designsystem.components.ChatAppModalBottomSheet
import com.ahmetocak.designsystem.components.ChatAppScaffold
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.Friend
import com.ahmetocak.model.LoadingState
import com.ahmetocak.ui.ChatItem

@Composable
internal fun ChatsRoute(
    onNavigateToChatBox: (Int, String?, String?, String?) -> Unit,
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
                    state.friendshipId,
                    state.friendEmail,
                    state.friendUsername,
                    state.friendProfPicUrl
                )
            }

            is NavigationState.Settings -> performNavigation { onNavigateSettings() }
            is NavigationState.None -> Unit
        }
    }

    if (uiState.showAddFriendBottomSheet) {
        AddFriendBottomSheet(
            friendValue = uiState.friendValue,
            onEvent = onEvent,
            isLoading = uiState.addFriendLoadingState == LoadingState.Loading,
            errorMessage = uiState.addFriendErrorMessage
        )
    }

    ChatAppScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ChatsScreenTopBar(onEvent = onEvent)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(ChatsUiEvent.OnShowAddFriendSheetClick) }) {
                Icon(
                    imageVector = ChatAppIcons.Filled.add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        ChatsScreen(
            modifier = Modifier.padding(paddingValues),
            friendList = uiState.friendList,
            onEvent = onEvent
        )
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
                title = chat.friendEmail ?: "deleted account",
                imageUrl = chat.friendProfilePicUrl,
                onClick = {
                    onEvent(
                        ChatsUiEvent.OnChatItemClick(
                            chat.id,
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
private fun AddFriendBottomSheet(
    friendValue: String,
    errorMessage: UiText?,
    isLoading: Boolean,
    onEvent: (ChatsUiEvent) -> Unit
) {
    ChatAppModalBottomSheet(
        onDismissRequest = remember { { onEvent(ChatsUiEvent.OnDismissAddFriendSheet) } },
        value = friendValue,
        onValueChange = remember { { onEvent(ChatsUiEvent.OnFriendValueChanged(it)) } },
        onSubmitClick = remember { { onEvent(ChatsUiEvent.OnAddFriendClick) } },
        labelText = errorMessage?.asString() ?: "Friend Email",
        placeholderText = "Enter friend email",
        leadingIcon = {
            Icon(imageVector = ChatAppIcons.Filled.email, contentDescription = null)
        },
        isLoading = isLoading,
        isError = errorMessage != null,
        title = "Add Friend",
        keyboardActions = KeyboardActions(onDone = remember { { onEvent(ChatsUiEvent.OnAddFriendClick) } }),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
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