package com.ahmetocak.ui.chat_bubble

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.R
import com.ahmetocak.designsystem.components.NetworkImage

internal val AuthorImgHeight = 16.dp

private val imgModifier = Modifier
    .height(AuthorImgHeight)
    .clip(CircleShape)
    .aspectRatio(1f)

@Composable
internal fun AuthorImage(authorImgUrl: String?) {
    authorImgUrl?.let {
        NetworkImage(
            imageUrl = it,
            modifier = imgModifier
        )
    } ?: run {
        Icon(
            painter = painterResource(id = R.drawable.blank_profile),
            contentDescription = null,
            modifier = imgModifier
        )
    }
}

val dateTextStyle @Composable
get() = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)

val authorTextStyle @Composable
get() = MaterialTheme.typography.titleMedium