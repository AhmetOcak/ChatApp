package com.ahmetocak.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ChatAppDialog(
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - 32.dp),
            shape = RoundedCornerShape(28.dp),
            content = content
        )
    }
}