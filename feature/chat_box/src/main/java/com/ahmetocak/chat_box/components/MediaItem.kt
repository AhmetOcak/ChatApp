package com.ahmetocak.chat_box.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.ahmetocak.designsystem.components.NetworkImage
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.model.MessageType

@Composable
internal fun MediaItem(
    modifier: Modifier = Modifier,
    messageType: MessageType,
    messageContent: String,
    onClick: (String) -> Unit
) {
    Card(modifier = modifier, onClick = { onClick(messageContent) }) {
        when (messageType) {
            MessageType.DOC -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(imageVector = ChatAppIcons.Filled.document, contentDescription = null)
                }
            }

            MessageType.AUDIO -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(imageVector = ChatAppIcons.Filled.audioFile, contentDescription = null)
                }
            }

            else -> {
                NetworkImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f),
                    imageUrl = messageContent,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}