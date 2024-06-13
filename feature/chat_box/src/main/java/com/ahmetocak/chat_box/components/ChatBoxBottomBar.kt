package com.ahmetocak.chat_box.components

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ahmetocak.chat_box.ChatBoxUiEvent
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.model.MessageType
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@Composable
internal fun ChatBoxBottomBar(
    messageValue: String,
    onEvent: (ChatBoxUiEvent) -> Unit,
    isAudioRecording: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
            .padding(bottom = 8.dp)
            .height(BottomBarHeight)
    ) {
        Surface(modifier = Modifier.weight(5f), shape = CircleShape) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MessageField(
                    messageValue = messageValue,
                    onEvent = onEvent,
                    isAudioRecording = isAudioRecording
                )
            }
        }
        BottomBarActionButton(
            messageValue = messageValue,
            onEvent = onEvent,
            isAudioRecording = isAudioRecording
        )
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun RowScope.BottomBarActionButton(
    messageValue: String,
    onEvent: (ChatBoxUiEvent) -> Unit,
    isAudioRecording: Boolean
) {
    val microphonePermissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )
    val context = LocalContext.current

    IconButton(
        onClick = remember(messageValue) {
            {
                if (messageValue.isBlank()) {
                    onEvent(
                        ChatBoxUiEvent.OnMicrophonePress(context = context) {
                            return@OnMicrophonePress if (microphonePermissionState.status.isGranted) {
                                true
                            } else {
                                microphonePermissionState.launchPermissionRequest()
                                false
                            }
                        }
                    )
                } else {
                    onEvent(ChatBoxUiEvent.OnSendMessageClick(messageType = MessageType.TEXT))
                }
            }
        },
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .padding(8.dp),
        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        AnimatedVisibility(visible = messageValue.isNotBlank()) {
            Icon(
                imageVector = ChatAppIcons.Default.send,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background
            )
        }
        AnimatedVisibility(visible = messageValue.isBlank() && !isAudioRecording) {
            Icon(
                imageVector = ChatAppIcons.Filled.microphone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background
            )
        }
        AnimatedVisibility(visible = messageValue.isBlank() && isAudioRecording) {
            Icon(
                imageVector = ChatAppIcons.Filled.stop,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}

@Composable
private fun MessageField(
    messageValue: String,
    onEvent: (ChatBoxUiEvent) -> Unit,
    isAudioRecording: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "recording text")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFFF3131),
        targetValue = Color(0xFFC41E3A),
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "color"
    )

    TextField(
        modifier = Modifier.fillMaxSize(),
        value = messageValue,
        onValueChange = remember { { onEvent(ChatBoxUiEvent.OnMessageValueChange(it)) } },
        singleLine = true,
        trailingIcon = {
            Row {
                ChatAppIconButton(
                    onClick = remember { { onEvent(ChatBoxUiEvent.OnAttachMenuClick) } },
                    imageVector = ChatAppIcons.Filled.attach,
                )
                AnimatedVisibility(visible = messageValue.isBlank()) {
                    ChatAppIconButton(
                        onClick = remember { { onEvent(ChatBoxUiEvent.OnCameraClick) } },
                        imageVector = ChatAppIcons.Outlined.camera
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = if (isAudioRecording) "Recording..." else "Message",
                color = if (isAudioRecording) animatedColor else Color.Unspecified
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        enabled = !isAudioRecording
    )
}

internal val BottomBarHeight = 56.dp