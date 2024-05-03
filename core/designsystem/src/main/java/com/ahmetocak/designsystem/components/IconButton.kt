package com.ahmetocak.designsystem.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ChatAppIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String? = null,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors()
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}