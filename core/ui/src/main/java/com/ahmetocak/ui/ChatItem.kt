package com.ahmetocak.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.theme.ChatAppTheme

@Composable
fun ChatItem(
    id: Int,
    title: String,
    imageUrl: String?,
    onClick: (Int) -> Unit,
    enabled: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = { onClick(id) },
        enabled = enabled
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            imageUrl?.let { url ->
                AnimatedNetworkImage(
                    imageUrl = url,
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(56.dp)
                        .clip(CircleShape)
                )
            } ?: run {
                BlankUserImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(56.dp)
                        .clip(CircleShape)
                )
            }
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
fun ChatItemPreview() {
    ChatAppTheme {
        Surface {
            ChatItem(
                id = 0,
                title = "Ahmet Ocak",
                imageUrl = null,
                onClick = {},
            )
        }
    }
}