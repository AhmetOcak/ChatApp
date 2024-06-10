package com.ahmetocak.chat_box.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.components.FilledChatAppIconButton

@Composable
fun AttachItem(onClick: () -> Unit, imageVector: ImageVector, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilledChatAppIconButton(
            modifier = Modifier.size(56.dp).aspectRatio(1f),
            onClick = onClick,
            imageVector = imageVector,
            tint = Color.Black
        )
        Text(text = title)
    }
}