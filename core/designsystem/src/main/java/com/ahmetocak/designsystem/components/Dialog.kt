package com.ahmetocak.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ahmetocak.designsystem.R

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

@Composable
fun ChatAppSubmitDialog(
    onDismissRequest: () -> Unit,
    onSubmitClick: () -> Unit,
    title: String,
    description: String,
    submitText: String,
    isLoading: Boolean = false,
    isSubmitEnabled: Boolean = true
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - 32.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.error
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = onSubmitClick, enabled = isSubmitEnabled && !isLoading) {
                        if (isLoading) {
                            ButtonCircularProgressIndicator()
                        } else {
                            Text(text = submitText)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatAppSubmitValueDialog(
    onDismissRequest: () -> Unit,
    onSubmitClick: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    description: String,
    submitText: String,
    textFieldLabel:  @Composable (() -> Unit)? = null,
    isLoading: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.width(LocalConfiguration.current.screenWidthDp.dp - 32.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.error
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = value,
                    onValueChange = onValueChange,
                    label = textFieldLabel,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    visualTransformation = visualTransformation
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = onSubmitClick, enabled = value.isNotBlank() && !isLoading) {
                        if (isLoading) {
                            ButtonCircularProgressIndicator()
                        } else {
                            Text(text = submitText)
                        }
                    }
                }
            }
        }
    }
}