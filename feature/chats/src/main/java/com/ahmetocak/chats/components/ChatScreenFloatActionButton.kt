package com.ahmetocak.chats.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.ahmetocak.chats.ChatsUiEvent
import com.ahmetocak.chats.ScreenState
import com.ahmetocak.designsystem.icons.ChatAppIcons

@Composable
internal fun ChatScreenFloatActionButton(
    screenState: ScreenState,
    onEvent: (ChatsUiEvent) -> Unit
) {
    AnimatedVisibility(
        visible = screenState is ScreenState.Chats,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        FloatingActionButton(onClick = remember { { onEvent(ChatsUiEvent.OnAddOrCreateContactClick) } }) {
            Icon(imageVector = ChatAppIcons.Filled.add, contentDescription = null)
        }
    }
    AnimatedVisibility(
        visible = screenState is ScreenState.SelectParticipantsForGroup,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        FloatingActionButton(onClick = remember { { onEvent(ChatsUiEvent.OnCreateGroupClick) } }) {
            Icon(imageVector = ChatAppIcons.Default.arrowForward, contentDescription = null)
        }
    }
    AnimatedVisibility(
        visible = screenState is ScreenState.CreateChatGroup,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        FloatingActionButton(onClick = remember { { onEvent(ChatsUiEvent.OnSubmitContactClick) } }) {
            Icon(imageVector = ChatAppIcons.Filled.ok, contentDescription = null)
        }
    }
}