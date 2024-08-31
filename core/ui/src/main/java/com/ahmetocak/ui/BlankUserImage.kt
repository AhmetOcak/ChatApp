package com.ahmetocak.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetocak.designsystem.components.LightDarkPreview
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.theme.ChatAppTheme

@Composable
fun BlankUserImage(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        imageVector = ChatAppIcons.Filled.account,
        contentDescription = null
    )
}

@LightDarkPreview
@Composable
private fun PreviewBlankUserImage() {
    ChatAppTheme {
        Surface {
            BlankUserImage()
        }
    }
}