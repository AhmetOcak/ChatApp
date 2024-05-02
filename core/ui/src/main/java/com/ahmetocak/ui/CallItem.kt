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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.R
import com.ahmetocak.designsystem.components.AnimatedAsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CallItem(
    id: String,
    imageUrl: String?,
    title: String,
    date: String,
    onClick: (String) -> Unit,
    onLongClick: (String) -> Unit,
    onImageClick: (String) -> Unit,
    callDirectionIcon: @Composable () -> Unit,
    callTypeIcon: ImageVector,
    isCallSuccessful: Boolean,
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
                    painter = painterResource(id = R.drawable.blank_profile),
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
                    overflow = TextOverflow.Ellipsis,
                    color = if (!isCallSuccessful) Color.Red else Color.Unspecified,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    callDirectionIcon()
                    Text(
                        text = date,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = callTypeIcon,
                contentDescription = null,
                modifier = Modifier.weight(1f),
                tint = Color.Green
            )
        }
    }
}