package com.ahmetocak.chats.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ahmetocak.chats.ChatsUiEvent
import com.ahmetocak.chats.ScreenState
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ChatsScreenTopBar(
    screenState: ScreenState,
    onEvent: (ChatsUiEvent) -> Unit
) {
    TopAppBar(
        title = {
            when (screenState) {
                ScreenState.Chats -> Text(text = "ChatApp")
                ScreenState.CreateChatRoomOrAddPerson -> Text(text = "Create Contact")
                ScreenState.AddPerson -> Text(text = "New Person")
            }
        },
        navigationIcon = {
            when (screenState) {
                ScreenState.Chats -> {}
                else -> {
                    ChatAppIconButton(
                        onClick = remember { { onEvent(ChatsUiEvent.OnBackClick) } },
                        imageVector = ChatAppIcons.Default.arrowBack
                    )
                }
            }
        },
        actions = {
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatsUiEvent.OnSettingsClick) } },
                imageVector = ChatAppIcons.Filled.settings
            )
        }
    )
}