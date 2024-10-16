package com.ahmetocak.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ahmetocak.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppModalBottomSheet(
    onDismissRequest: () -> Unit,
    value: String,
    onValueChange: (String) -> Unit,
    onCancelClick: () -> Unit = onDismissRequest,
    onSubmitClick: () -> Unit,
    title: String,
    labelText: String = "",
    placeholderText: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    isLoading: Boolean = false,
    isError: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_16)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_16)),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                label = {
                    Text(text = labelText)
                },
                placeholder = {
                    Text(text = placeholderText)
                },
                leadingIcon = leadingIcon,
                singleLine = true,
                isError = isError,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions
            )
            Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_16))) {
                TextButton(onClick = onCancelClick) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                TextButton(
                    onClick = onSubmitClick,
                    enabled = value.isNotBlank() && !isLoading
                ) {
                    if (isLoading) {
                        ButtonCircularProgressIndicator()
                    } else {
                        Text(text = stringResource(id = R.string.add))
                    }
                }
            }
        }
    }
}