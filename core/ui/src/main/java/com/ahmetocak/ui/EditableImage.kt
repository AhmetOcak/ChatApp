package com.ahmetocak.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons

@Composable
fun EditableImage(
    imageUrl: String?,
    onPickImageClick: () -> Unit,
    isImageUploading: Boolean
) {
    val size = (LocalConfiguration.current.screenWidthDp / 2.5f).dp

    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (isImageUploading) {
            CircularProgressIndicator()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            ) {
                imageUrl?.let { url ->
                    AnimatedNetworkImage(imageUrl = url, modifier = Modifier.fillMaxSize())
                } ?: run {
                    BlankUserImage(
                        modifier = Modifier.fillMaxSize().aspectRatio(1f)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                ChatAppIconButton(
                    onClick = onPickImageClick,
                    imageVector = ChatAppIcons.Outlined.camera,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}