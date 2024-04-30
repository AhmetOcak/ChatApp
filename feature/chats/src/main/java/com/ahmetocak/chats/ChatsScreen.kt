package com.ahmetocak.chats

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.ChatAppProgressIndicator
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.LoadingState
import com.ahmetocak.model.UserChat
import com.ahmetocak.ui.ChatItem

@Composable
internal fun ChatsRoute(
    onNavigateToChatBox: (String) -> Unit,
    onChatLongClick: (String) -> Unit,
    modifier: Modifier = Modifier,
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
            is NavigationState.ChatBox -> performNavigation { onNavigateToChatBox(state.id) }
            is NavigationState.None -> Unit
        }
    }

    ChatAppProgressIndicator(visible = uiState.loadingState is LoadingState.Loading)

    ChatsScreen(
        modifier = modifier,
        onChatLongClick = onChatLongClick,
        chatList = uiState.chatList,
        onEvent = onEvent
    )
}

@Composable
internal fun ChatsScreen(
    modifier: Modifier = Modifier,
    onChatLongClick: (String) -> Unit,
    chatList: List<UserChat>,
    onEvent: (ChatsUiEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(chatList, key = { it.id }) { chat ->
            ChatItem(
                id = chat.id,
                title = chat.title,
                imageUrl = chat.imageUrl,
                isSilent = chat.isSilent,
                lastMessage = chat.lastMessage,
                lastMessageTime = chat.lastMessageTime,
                onClick = { onEvent(ChatsUiEvent.OnChatItemClick(chat.id)) },
                onLongClick = onChatLongClick,
                onImageClick = { onEvent(ChatsUiEvent.OnImageClick) }
            )
        }
    }
}

@Preview
@Composable
fun ChatsScreenPreview() {
    ChatAppTheme {
        Surface {
            ChatsScreen(
                onChatLongClick = {},
                chatList = emptyList(),
                onEvent = {}
            )
        }
    }
}