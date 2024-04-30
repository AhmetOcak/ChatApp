package com.ahmetocak.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import com.ahmetocak.designsystem.components.ChatAppProgressIndicator
import com.ahmetocak.designsystem.components.ChatButton
import com.ahmetocak.designsystem.components.ChatImageButton
import com.ahmetocak.designsystem.components.ChatTextButton
import com.ahmetocak.designsystem.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.DialogState
import com.ahmetocak.model.LoadingState
import com.ahmetocak.designsystem.R.drawable as AppResources

@Composable
internal fun LoginRoute(
    navigateToChats: () -> Unit,
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: LoginEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(action: () -> Unit) {
            action()
            viewModel.resetNavigation()
        }

        when(navigationState) {
            NavigationState.SignUp -> performNavigation(navigateToSignUp)
            NavigationState.Chats -> performNavigation(navigateToChats)
            NavigationState.None -> Unit
        }
    }

    if (uiState.loadingState is LoadingState.Loading) ChatAppProgressIndicator()

    LoginScreen(
        modifier = modifier,
        emailValue = uiState.email,
        isEmailFieldError = false,
        emailFieldLabelText = "Email",
        passwordValue = uiState.password,
        isPasswordFieldError = false,
        passwordFieldLabelText = "Password",
        onEvent = { event -> onEvent(event) }
    )
}

@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    emailValue: String,
    isEmailFieldError: Boolean,
    emailFieldLabelText: String,
    passwordValue: String,
    isPasswordFieldError: Boolean,
    passwordFieldLabelText: String,
    onEvent: (LoginEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        WelcomeMessage()
        AuthInputSection(
            emailValue = emailValue,
            isEmailFieldError = isEmailFieldError,
            emailFieldLabelText = emailFieldLabelText,
            passwordValue = passwordValue,
            isPasswordFieldError = isPasswordFieldError,
            passwordFieldLabelText = passwordFieldLabelText,
            onEvent = onEvent
        )
        SubmitLoginSection(onEvent = onEvent)
    }
}

@Composable
private fun WelcomeMessage() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Welcome", style = MaterialTheme.typography.displayLarge)
        Text(text = "Sign in to continue", style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
private fun AuthInputSection(
    emailValue: String,
    isEmailFieldError: Boolean,
    emailFieldLabelText: String,
    passwordValue: String,
    isPasswordFieldError: Boolean,
    passwordFieldLabelText: String,
    onEvent: (LoginEvent) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AuthEmailOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = emailValue,
            onValueChange = { onEvent(LoginEvent.OnEmailChanged(it)) },
            isError = isEmailFieldError,
            labelText = emailFieldLabelText
        )
        AuthPasswordOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            onValueChange = { onEvent(LoginEvent.OnPasswordChanged(it)) },
            labelText = passwordFieldLabelText,
            isError = isPasswordFieldError
        )
    }
}

@Composable
private fun SubmitLoginSection(onEvent: (LoginEvent) -> Unit) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChatButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(LoginEvent.OnLoginClicked) }
        ) {
            Text(text = "LOGIN")
        }
        ChatTextButton(
            onClick = { onEvent(LoginEvent.OnForgotPasswordClick(DialogState.Show)) },
            text = "Forgot Password?"
        )
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
            onClick = { onEvent(LoginEvent.OnGoogleClicked(context)) },
            image = AppResources.ic_google
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account?")
            ChatTextButton(onClick = { onEvent(LoginEvent.OnSignUpClicked) }, text = "Sign up")
        }
    }
}

@Preview(
    device = "id:pixel_8_pro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_UNDEFINED
)
@Composable
fun LoginScreenPreview() {
    ChatAppTheme {
        LoginScreen(
            emailValue = "",
            isEmailFieldError = false,
            emailFieldLabelText = "Email",
            passwordValue = "",
            isPasswordFieldError = false,
            passwordFieldLabelText = "Password",
            onEvent = { _ -> }
        )
    }
}