package com.ahmetocak.ui.chat_bubble

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.ahmetocak.designsystem.components.NetworkImage
import com.ahmetocak.ui.BlankUserImage
import com.ahmetocak.ui.R

private val imgModifier @Composable
get() = Modifier
    .height(dimensionResource(id = R.dimen.author_img_height))
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
        BlankUserImage(modifier = imgModifier)
    }
}

val dateTextStyle @Composable
get() = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)

val authorTextStyle @Composable
get() = MaterialTheme.typography.titleMedium