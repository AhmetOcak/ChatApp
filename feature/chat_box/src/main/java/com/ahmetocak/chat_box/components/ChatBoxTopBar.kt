package com.ahmetocak.chat_box.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ahmetocak.chat_box.ChatBoxUiEvent
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.ui.BlankUserImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatBoxTopBar(
    chatBoxTitle: String,
    imageUrl: String?,
    onEvent: (ChatBoxUiEvent) -> Unit
) {
    TopAppBar(
        title = {
            Text(text = chatBoxTitle, style = MaterialTheme.typography.titleMedium)
        },
        actions = {
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnCameraClick) } },
                imageVector = ChatAppIcons.Outlined.camera
            )
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnMenuClick) } },
                imageVector = ChatAppIcons.Filled.moreVert
            )
        },
        navigationIcon = {
            Surface(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnBackClick) } },
                shape = RoundedCornerShape(20)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ChatAppIcons.Default.arrowBack, contentDescription = null)
                    imageUrl?.let { url ->
                        AnimatedNetworkImage(
                            imageUrl = url,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    } ?: run {
                        BlankUserImage(
                            Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    )
}