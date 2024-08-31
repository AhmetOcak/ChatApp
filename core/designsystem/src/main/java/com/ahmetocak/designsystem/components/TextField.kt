package com.ahmetocak.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.theme.ChatAppTheme

@Composable
fun ChatAppOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    placeholderText: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        label = if (labelText != null) {
            { Text(text = labelText) }
        } else null,
        placeholder = if (placeholderText != null) {
            { Text(text = placeholderText) }
        } else null,
        leadingIcon = if (leadingIcon != null) {
            { Icon(imageVector = leadingIcon, contentDescription = null) }
        } else null,
        trailingIcon = if (trailingIcon != null) {
            { Icon(imageVector = trailingIcon, contentDescription = null) }
        } else null,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError
    )
}

@Composable
fun ChatAppFilledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    placeholderText: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = if (labelText != null) {
            { Text(text = labelText) }
        } else null,
        placeholder = if (placeholderText != null) {
            { Text(text = placeholderText) }
        } else null,
        leadingIcon = if (leadingIcon != null) {
            { Icon(imageVector = leadingIcon, contentDescription = null) }
        } else null,
        trailingIcon = if (trailingIcon != null) {
            { Icon(imageVector = trailingIcon, contentDescription = null) }
        } else null,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError
    )
}

@LightDarkPreview
@Composable
private fun PreviewChatAppOutlinedTextField() {
    ChatAppTheme {
        Surface {
            ChatAppOutlinedTextField(
                modifier = Modifier.padding(8.dp),
                value = "",
                onValueChange = {},
                placeholderText = "Placeholder",
                leadingIcon = Icons.Filled.Person,
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun PreviewChatAppFilledTextField() {
    ChatAppTheme {
        Surface {
            ChatAppFilledTextField(
                modifier = Modifier.padding(8.dp),
                value = "",
                onValueChange = {},
                placeholderText = "Placeholder",
                leadingIcon = Icons.Filled.Person
            )
        }
    }
}