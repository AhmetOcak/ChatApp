package com.ahmetocak.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.R.drawable as AppResources
import com.ahmetocak.designsystem.components.AnimatedAsyncImage
import com.ahmetocak.designsystem.theme.ChatAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    id: String,
    title: String,
    imageUrl: String?,
    isSilent: Boolean,
    lastMessage: String,
    lastMessageTime: String,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    onImageClick: (String) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick(id) },
                onLongClick = { onLongClick(id) }
            ),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            imageUrl?.let { url ->
                AnimatedAsyncImage(
                    imageUrl = url,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable(onClick = { onImageClick(id) })
                )
            } ?: run {
                Image(
                    painter = painterResource(id = AppResources.blank_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable(onClick = { onImageClick(id) })
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(5f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = lastMessage,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = lastMessageTime,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                if (isSilent) {
                    Icon(
                        imageVector = Icons.Filled.NotificationsOff,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatItemPreview() {
    ChatAppTheme {
        Surface {
            ChatItem(
                id = "",
                title = "Ahmet Ocak",
                imageUrl = null,
                isSilent = false,
                lastMessage = "Hello There",
                lastMessageTime = "12:03",
                onClick = {},
                onLongClick = {},
                onImageClick = {}
            )
        }
    }
}

@Preview
@Composable
fun ChatItemPreviewSilent() {
    ChatAppTheme {
        Surface {
            ChatItem(
                id = "",
                title = "Ahmet Ocak",
                imageUrl = null,
                isSilent = true,
                lastMessage = "Hello There",
                lastMessageTime = "12:03",
                onClick = {},
                onLongClick = {},
                onImageClick = {}
            )
        }
    }
}