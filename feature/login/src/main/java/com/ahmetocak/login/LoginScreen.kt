package com.ahmetocak.login

import android.app.Activity.RESULT_OK
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.ChatAppGreyProgressIndicator
import com.ahmetocak.designsystem.components.ChatAppSubmitValueDialog
import com.ahmetocak.designsystem.components.ChatAppButton
import com.ahmetocak.designsystem.components.ChatAppImageButton
import com.ahmetocak.designsystem.components.ChatAppTextButton
import com.ahmetocak.designsystem.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.LoadingState
import com.ahmetocak.designsystem.R.drawable as AppResources
import com.ahmetocak.designsystem.R.string as AppStrings
import com.ahmetocak.designsystem.R.dimen as ChatAppDimen

@Composable
internal fun LoginRoute(
    navigateToChats: () -> Unit,
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: LoginUiEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(action: () -> Unit) {
            action()
            viewModel.resetNavigation()
        }

        when (navigationState) {
            NavigationState.SignUp -> performNavigation(navigateToSignUp)
            NavigationState.Chats -> performNavigation(navigateToChats)
            NavigationState.None -> Unit
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { intent ->
                    viewModel.googleSignIn(intent)
                }
            }
        }
    )

    if (uiState.loadingState is LoadingState.Loading && !uiState.showForgotPasswordDialog) {
        ChatAppGreyProgressIndicator()
    }

    LoginScreen(
        modifier = modifier,
        emailValue = uiState.email,
        isEmailFieldError = false,
        emailFieldLabelText = stringResource(id = AppStrings.email),
        passwordValue = uiState.password,
        isPasswordFieldError = false,
        passwordFieldLabelText = stringResource(id = AppStrings.password),
        onEvent = remember { { event -> onEvent(event) } },
        onSignInWithGoogleClick = remember {
            {
                onEvent(LoginUiEvent.OnGoogleClicked { intentSenderRequest ->
                    launcher.launch(intentSenderRequest)
                })
            }
        }
    )

    if (uiState.showForgotPasswordDialog) {
        ChatAppSubmitValueDialog(
            onDismissRequest = viewModel::hideResetPasswordDialog,
            onSubmitClick = remember { { onEvent(LoginUiEvent.OnSendPasswordResetMailClick) } },
            onValueChange = remember { { onEvent(LoginUiEvent.OnResetEmailChanged(it)) } },
            title = stringResource(id = AppStrings.password_reset),
            description = stringResource(id = AppStrings.password_reset_description),
            submitText = stringResource(id = AppStrings.send),
            value = uiState.resetEmail,
            isLoading = uiState.loadingState == LoadingState.Loading
        )
    }
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
    onEvent: (LoginUiEvent) -> Unit,
    onSignInWithGoogleClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = ChatAppDimen.padding_16)),
        verticalArrangement = Arrangement.Center
    ) {
        WelcomeMessage()
        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_64)))
        AuthInputSection(
            emailValue = emailValue,
            isEmailFieldError = isEmailFieldError,
            emailFieldLabelText = emailFieldLabelText,
            passwordValue = passwordValue,
            isPasswordFieldError = isPasswordFieldError,
            passwordFieldLabelText = passwordFieldLabelText,
            onEvent = onEvent
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_64)))
        SubmitLoginSection(onEvent = onEvent, onSignInWithGoogleClick = onSignInWithGoogleClick)
    }
}

@Composable
private fun WelcomeMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16))
    ) {
        Text(
            text = stringResource(id = AppStrings.welcome),
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = stringResource(id = AppStrings.sign_in_to_continue),
            style = MaterialTheme.typography.displaySmall
        )
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
    onEvent: (LoginUiEvent) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16))
    ) {
        AuthEmailOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = emailValue,
            onValueChange = remember { { onEvent(LoginUiEvent.OnEmailChanged(it)) } },
            isError = isEmailFieldError,
            labelText = emailFieldLabelText
        )
        AuthPasswordOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = passwordValue,
            onValueChange = remember { { onEvent(LoginUiEvent.OnPasswordChanged(it)) } },
            labelText = passwordFieldLabelText,
            isError = isPasswordFieldError
        )
    }
}

@Composable
private fun SubmitLoginSection(
    onEvent: (LoginUiEvent) -> Unit,
    onSignInWithGoogleClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_8))
    ) {
        ChatAppButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = remember { { onEvent(LoginUiEvent.OnLoginClickedUi) } },
            text = stringResource(id = AppStrings.login).uppercase()
        )
        ChatAppTextButton(
            onClick = remember { { onEvent(LoginUiEvent.OnForgotPasswordClick) } },
            text = stringResource(id = AppStrings.forgot_password)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = ChatAppDimen.padding_8)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_8)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(text = "or")
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        ChatAppImageButton(
            onClick = onSignInWithGoogleClick,
            image = AppResources.ic_google
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = AppStrings.no_account))
            ChatAppTextButton(
                onClick = remember { { onEvent(LoginUiEvent.OnSignUpClicked) } },
                text = stringResource(id = AppStrings.sign_up)
            )
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
            onEvent = { _ -> },
            onSignInWithGoogleClick = {}
        )
    }
}