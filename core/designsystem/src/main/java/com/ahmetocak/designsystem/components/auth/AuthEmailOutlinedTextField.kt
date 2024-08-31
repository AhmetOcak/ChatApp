package com.ahmetocak.designsystem.components.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ahmetocak.designsystem.components.LightDarkPreview
import com.ahmetocak.designsystem.theme.ChatAppTheme

@Composable
fun AuthEmailOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    labelText: String
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = labelText)
        },
        trailingIcon = {
            Icon(imageVector = Icons.Filled.Email, contentDescription = null)
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = isError,
        singleLine = true
    )
}

@Composable
fun AuthPasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    labelText: String
) {
    var visibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = labelText)
        },
        trailingIcon = {
            IconButton(onClick = { visibility = !visibility }) {
                Icon(
                    imageVector = if (visibility) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    contentDescription = null
                )
            }
        },
        visualTransformation = if (visibility) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        isError = isError
    )
}

@LightDarkPreview
@Composable
private fun PreviewAuthEmailOutlinedTextField() {
    ChatAppTheme {
        Surface {
            AuthEmailOutlinedTextField(
                modifier = Modifier.padding(8.dp),
                value = "",
                onValueChange = {},
                labelText = "Label"
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun PreviewAuthPasswordOutlinedTextField() {
    ChatAppTheme {
        Surface {
            AuthPasswordOutlinedTextField(
                modifier = Modifier.padding(8.dp),
                value = "",
                onValueChange = {},
                labelText = "Label"
            )
        }
    }
}