package com.ahmetocak.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetocak.designsystem.theme.ChatAppTheme

@Composable
fun ChatAppIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String? = null,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    tint: Color = LocalContentColor.current
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription, tint = tint)
    }
}

@Composable
fun FilledChatAppIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String? = null,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors()
) {
    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}

@Composable
fun ChatAppAnimatedIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    imageVector2: ImageVector,
    animationCondition: Boolean,
    contentDescription: String? = null,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    enterAnimation: EnterTransition = scaleIn(),
    exitAnimation: ExitTransition = scaleOut()
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors
    ) {
        AnimatedVisibility(
            visible = animationCondition,
            enter = enterAnimation,
            exit = exitAnimation
        ) {
            Icon(imageVector = imageVector, contentDescription = contentDescription)
        }
        AnimatedVisibility(
            visible = !animationCondition,
            enter = enterAnimation,
            exit = exitAnimation
        ) {
            Icon(imageVector = imageVector2, contentDescription = contentDescription)
        }
    }
}

@LightDarkPreview
@Composable
private fun PreviewChatAppIconButton() {
    ChatAppTheme {
        Surface {
            ChatAppIconButton(onClick = { /*TODO*/ }, imageVector = Icons.Filled.Add)
        }
    }
}

@LightDarkPreview
@Composable
private fun PreviewFilledChatAppIconButton() {
    ChatAppTheme {
        Surface {
            FilledChatAppIconButton(onClick = { /*TODO*/ }, imageVector = Icons.Filled.Add)
        }
    }
}

@LightDarkPreview
@Composable
private fun PreviewChatAppAnimatedIconButton() {
    ChatAppTheme {
        Surface {
            ChatAppAnimatedIconButton(
                onClick = { /*TODO*/ },
                imageVector = Icons.Filled.Add,
                imageVector2 = Icons.Filled.Close,
                animationCondition = true
            )
        }
    }
}