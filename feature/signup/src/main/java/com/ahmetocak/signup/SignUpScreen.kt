package com.ahmetocak.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.ChatAppOutlinedTextField
import com.ahmetocak.designsystem.components.ChatButton
import com.ahmetocak.designsystem.components.ChatImageButton
import com.ahmetocak.designsystem.components.ChatTextButton
import com.ahmetocak.designsystem.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.designsystem.R.drawable as AppResources

@Composable
internal fun SignUpRoute(
    navigateUp: () -> Unit,
    navigateToChats: () -> Unit,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: SignUpEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(action: () -> Unit) {
            action()
            viewModel.resetNavigationState()
        }

        when (navigationState) {
            NavigationState.NavigateUp -> performNavigation(navigateUp)
            NavigationState.Login -> performNavigation(navigateToLogin)
            NavigationState.Chats -> performNavigation(navigateToChats)
            NavigationState.None -> Unit
        }
    }

    SignUpScreen(
        modifier = modifier,
        onEvent = onEvent,
        nameValue = uiState.name,
        isNameFieldError = false,
        nameFieldLabelText = "Username",
        emailValue = uiState.email,
        isEmailFieldError = false,
        emailLabelText = "Email",
        passwordValue = uiState.password,
        isPasswordFieldError = false,
        passwordLabelText = "Password",
        confirmPasswordValue = uiState.confirmPassword,
        isConfirmPasswordError = false,
        confirmPasswordFieldLabelText = "Confirm Password"
    )
}

@Composable
internal fun SignUpScreen(
    modifier: Modifier,
    onEvent: (SignUpEvent) -> Unit,
    nameValue: String,
    isNameFieldError: Boolean,
    nameFieldLabelText: String,
    emailValue: String,
    isEmailFieldError: Boolean,
    emailLabelText: String,
    passwordValue: String,
    isPasswordFieldError: Boolean,
    passwordLabelText: String,
    confirmPasswordValue: String,
    isConfirmPasswordError: Boolean,
    confirmPasswordFieldLabelText: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        WelcomeMessage()
        InputSection(
            onEvent = onEvent,
            nameValue = nameValue,
            isNameFieldError = isNameFieldError,
            emailValue = emailValue,
            isEmailFieldError = isEmailFieldError,
            emailLabelText = emailLabelText,
            passwordValue = passwordValue,
            isPasswordFieldError = isPasswordFieldError,
            passwordLabelText = passwordLabelText,
            confirmPasswordValue = confirmPasswordValue,
            isConfirmPasswordError = isConfirmPasswordError,
            confirmPasswordFieldLabelText = confirmPasswordFieldLabelText,
            nameFieldLabelText = nameFieldLabelText
        )
        SubmitSignUpSection(onEvent = onEvent)
    }
}

@Composable
private fun WelcomeMessage() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Hi!", style = MaterialTheme.typography.displayLarge)
        Text(text = "Create a new account", style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
private fun InputSection(
    onEvent: (SignUpEvent) -> Unit,
    nameValue: String,
    isNameFieldError: Boolean,
    nameFieldLabelText: String,
    emailValue: String,
    isEmailFieldError: Boolean,
    emailLabelText: String,
    passwordValue: String,
    isPasswordFieldError: Boolean,
    passwordLabelText: String,
    confirmPasswordValue: String,
    isConfirmPasswordError: Boolean,
    confirmPasswordFieldLabelText: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ChatAppOutlinedTextField(
            value = nameValue,
            onValueChange = { onEvent(SignUpEvent.OnNameChanged(it)) },
            isError = isNameFieldError,
            label = {
                Text(text = nameFieldLabelText)
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Person, contentDescription = null)
            }
        )
        AuthEmailOutlinedTextField(
            value = emailValue,
            onValueChange = { onEvent(SignUpEvent.OnEmailChanged(it)) },
            isError = isEmailFieldError,
            labelText = emailLabelText,
            modifier = Modifier.fillMaxWidth()
        )
        AuthPasswordOutlinedTextField(
            value = passwordValue,
            onValueChange = { onEvent(SignUpEvent.OnPasswordChange(it)) },
            isError = isPasswordFieldError,
            labelText = passwordLabelText,
            modifier = Modifier.fillMaxWidth()
        )
        AuthPasswordOutlinedTextField(
            value = confirmPasswordValue,
            onValueChange = { onEvent(SignUpEvent.OnConfirmPasswordChange(it)) },
            isError = isConfirmPasswordError,
            labelText = confirmPasswordFieldLabelText,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SubmitSignUpSection(onEvent: (SignUpEvent) -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChatButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(SignUpEvent.OnSignUpClick) }
        ) {
            Text(text = "SIGN UP")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(text = "or")
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        ChatImageButton(
            onClick = { onEvent(SignUpEvent.OnGoogleClicked(context)) },
            image = AppResources.ic_google
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account?")
            ChatTextButton(
                onClick = { onEvent(SignUpEvent.OnAlreadyHaveAccountClick) },
                text = "Sign in"
            )
        }
    }
}

@Preview(device = "id:pixel_8_pro", showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(
        modifier = Modifier,
        onEvent = {},
        nameValue = "",
        isNameFieldError = false,
        nameFieldLabelText = "Username",
        emailValue = "",
        isEmailFieldError = false,
        emailLabelText = "Email",
        passwordValue = "",
        isPasswordFieldError = false,
        passwordLabelText = "Password",
        confirmPasswordValue = "",
        isConfirmPasswordError = false,
        confirmPasswordFieldLabelText = "Confirm Password"
    )
}