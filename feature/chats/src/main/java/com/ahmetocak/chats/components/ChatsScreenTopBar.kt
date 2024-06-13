package com.ahmetocak.chats.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ahmetocak.chats.ChatsUiEvent
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ChatsScreenTopBar(onEvent: (ChatsUiEvent) -> Unit) {
    TopAppBar(
        title = {
            Text(text = "ChatApp")
        },
        actions = {
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatsUiEvent.OnSettingsClick) } },
                imageVector = ChatAppIcons.Filled.settings
            )
        }
    )
}