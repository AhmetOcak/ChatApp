package com.ahmetocak.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.R
import com.ahmetocak.designsystem.theme.ChatAppTheme

@Composable
fun ChatAppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    text: String
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(dimensionResource(id = R.dimen.btn_height)),
        enabled = enabled,
        contentPadding = contentPadding,
        content = {
            Text(text = text)
        }
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
        modifier = modifier.size(dimensionResource(id = R.dimen.img_btn_size)),
        shape = shape,
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_4)),
            painter = painterResource(id = image),
            contentDescription = null
        )
    }
}

@LightDarkPreview
@Composable
private fun PreviewChatAppButton() {
    ChatAppTheme {
        Surface {
            ChatAppButton(onClick = { /*TODO*/ }, text = "click")
        }
    }
}

@LightDarkPreview
@Composable
private fun PreviewChatAppTextButton() {
    ChatAppTheme {
        Surface {
            ChatAppTextButton(onClick = { /*TODO*/ }, text = "Click")
        }
    }
}

@LightDarkPreview
@Composable
private fun PreviewChatAppImageButton() {
    ChatAppTheme {
        Surface {
            ChatAppImageButton(onClick = { /*TODO*/ }, image = R.drawable.ic_google)
        }
    }
}
