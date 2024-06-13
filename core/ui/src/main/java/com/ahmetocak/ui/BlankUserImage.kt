package com.ahmetocak.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ahmetocak.designsystem.icons.ChatAppIcons

@Composable
fun BlankUserImage(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        imageVector = ChatAppIcons.Filled.account,
        contentDescription = null
    )
}