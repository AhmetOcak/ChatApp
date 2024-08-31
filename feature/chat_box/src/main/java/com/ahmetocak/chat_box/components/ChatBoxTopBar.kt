package com.ahmetocak.chat_box.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ahmetocak.chat_box.ChatBoxUiEvent
import com.ahmetocak.chat_box.R
import com.ahmetocak.chat_box.ScreenState
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.ui.BlankUserImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChatBoxTopBar(
    chatBoxTitle: String,
    imageUrl: String?,
    onEvent: (ChatBoxUiEvent) -> Unit,
    screenState: ScreenState
) {
    var isExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            when (screenState) {
                is ScreenState.ChatBox -> {
                    Text(text = chatBoxTitle, style = MaterialTheme.typography.titleMedium)
                }

                is ScreenState.GroupInfo -> Text(text = stringResource(id = R.string.group_info))
                is ScreenState.GroupMedia -> Text(text = stringResource(id = R.string.group_media))
                is ScreenState.AddParticipant -> Text(text = stringResource(id = R.string.add_participant))
            }
        },
        actions = {
            if (screenState is ScreenState.ChatBox) {
                ChatAppIconButton(
                    onClick = remember { { onEvent(ChatBoxUiEvent.OnCameraClick) } },
                    imageVector = ChatAppIcons.Outlined.camera
                )
                ChatAppIconButton(
                    onClick = remember { { isExpanded = true } },
                    imageVector = ChatAppIcons.Filled.moreVert
                )
                DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.group_info)) },
                        onClick = {
                            onEvent(ChatBoxUiEvent.OnGroupInfoClick)
                            isExpanded = false
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.group_media)) },
                        onClick = {
                            onEvent(ChatBoxUiEvent.OnGroupMediaClick)
                            isExpanded = false
                        })
                }
            }
        },
        navigationIcon = {
            if (screenState is ScreenState.ChatBox) {
                Surface(
                    onClick = remember { { onEvent(ChatBoxUiEvent.OnBackClick) } },
                    shape = RoundedCornerShape(20)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ChatAppIcons.Default.arrowBack,
                            contentDescription = null
                        )
                        imageUrl?.let { url ->
                            AnimatedNetworkImage(
                                imageUrl = url,
                                modifier = Modifier
                                    .size(dimensionResource(id = R.dimen.friend_or_group_img_size))
                                    .clip(CircleShape)
                            )
                        } ?: run {
                            BlankUserImage(
                                Modifier
                                    .size(dimensionResource(id = R.dimen.friend_or_group_img_size))
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            } else {
                ChatAppIconButton(
                    onClick = remember { { onEvent(ChatBoxUiEvent.OnBackClick) } },
                    imageVector = ChatAppIcons.Default.arrowBack
                )
            }
        }
    )
}