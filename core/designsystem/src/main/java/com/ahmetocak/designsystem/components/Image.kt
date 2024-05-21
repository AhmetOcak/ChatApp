package com.ahmetocak.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.ahmetocak.designsystem.R.drawable as AppResources

@Composable
fun AnimatedNetworkImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .build()
    )

    val transition by animateFloatAsState(
        targetValue = if (painter.state is AsyncImagePainter.State.Success) 1f else 0f,
        label = "image transition animation"
    )

    val matrix = ColorMatrix()
    matrix.setToSaturation(transition)

    when (painter.state) {
        is AsyncImagePainter.State.Loading -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                modifier = modifier,
                painter = painter,
                contentDescription = null,
                colorFilter = ColorFilter.colorMatrix(matrix)
            )
        }

        is AsyncImagePainter.State.Error -> {
            Icon(
                modifier = modifier,
                painter = painterResource(id = AppResources.blank_profile),
                contentDescription = null
            )
        }

        is AsyncImagePainter.State.Empty -> Box(modifier = modifier)
    }
}

@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    imageUrl: String
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .memoryCacheKey(imageUrl)
            .diskCacheKey(imageUrl)
            .build()
    )

    when (painter.state) {
        is AsyncImagePainter.State.Loading -> Box(modifier = modifier)

        is AsyncImagePainter.State.Success -> {
            AnimatedVisibility(visible = true) {
                Image(
                    modifier = modifier,
                    painter = painter,
                    contentDescription = null
                )
            }
        }

        is AsyncImagePainter.State.Error -> {
            Icon(
                modifier = modifier,
                painter = painterResource(id = AppResources.blank_profile),
                contentDescription = null
            )
        }

        is AsyncImagePainter.State.Empty -> Box(modifier = modifier)
    }
}