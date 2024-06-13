package com.ahmetocak.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.components.ChatButtonDefaults.ImageButtonSize

@Composable
fun ChatAppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ChatButtonDefaults.ButtonHeight),
        enabled = enabled,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun ChatAppTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
) {
    TextButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        Text(text = text, style = style)
    }
}

@Composable
fun ChatAppImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    @DrawableRes image: Int
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(ImageButtonSize),
        shape = shape,
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(id = image),
            contentDescription = null
        )
    }
}

object ChatButtonDefaults {
    val ButtonHeight = 56.dp
    val ImageButtonSize = 48.dp
}