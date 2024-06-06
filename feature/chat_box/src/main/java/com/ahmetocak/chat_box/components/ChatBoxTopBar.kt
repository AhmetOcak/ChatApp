package com.ahmetocak.chat_box.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetocak.chat_box.ChatBoxUiEvent
import com.ahmetocak.designsystem.R
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatBoxTopBar(
    chatBoxTitle: String,
    members: String,
    imageUrl: String?,
    onEvent: (ChatBoxUiEvent) -> Unit
) {
    TopAppBar(
        title = {
            if (members.isNotBlank()) {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = chatBoxTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = members,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            } else {
                Text(text = chatBoxTitle, style = MaterialTheme.typography.titleMedium)
            }
        },
        actions = {
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnCameraClick) } },
                imageVector = ChatAppIcons.Outlined.camera
            )
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnCallClick) } },
                imageVector = ChatAppIcons.Outlined.call
            )
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnMenuClick) } },
                imageVector = ChatAppIcons.Filled.moreVert
            )
        },
        navigationIcon = {
            Surface(
                onClick = { onEvent(ChatBoxUiEvent.OnBackClick) },
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
                        Icon(
                            painter = painterResource(id = R.drawable.blank_profile),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    )
}