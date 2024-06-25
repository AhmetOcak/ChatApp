package com.ahmetocak.signup

import android.app.Activity
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.ChatAppGreyProgressIndicator
import com.ahmetocak.designsystem.components.ChatAppOutlinedTextField
import com.ahmetocak.designsystem.components.ChatAppButton
import com.ahmetocak.designsystem.components.ChatAppImageButton
import com.ahmetocak.designsystem.components.ChatAppTextButton
import com.ahmetocak.designsystem.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.model.LoadingState
import com.ahmetocak.designsystem.R.drawable as AppResources
import com.ahmetocak.designsystem.R.string as AppStrings

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

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    viewModel.googleSignIn(intent)
                }
            }
        }
    )

    if (uiState.loadingState is LoadingState.Loading) ChatAppGreyProgressIndicator()

    SignUpScreen(
        modifier = modifier,
        onEvent = onEvent,
        nameValue = uiState.name,
        emailValue = uiState.email,
        passwordValue = uiState.password,
        confirmPasswordValue = uiState.confirmPassword,
        onSignInWithGoogleClick = remember {
            {
                onEvent(SignUpEvent.OnGoogleClicked { intentSenderRequest ->
                    launcher.launch(intentSenderRequest)
                })
            }
        }
    )
}

@Composable
internal fun SignUpScreen(
    modifier: Modifier,
    onEvent: (SignUpEvent) -> Unit,
    nameValue: String,
    emailValue: String,
    passwordValue: String,
    confirmPasswordValue: String,
    onSignInWithGoogleClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        WelcomeMessage()
        Spacer(modifier = Modifier.height(32.dp))
        InputSection(
            onEvent = onEvent,
            nameValue = nameValue,
            emailValue = emailValue,
            passwordValue = passwordValue,
            confirmPasswordValue = confirmPasswordValue,
        )
        Spacer(modifier = Modifier.height(32.dp))
        SubmitSignUpSection(onEvent = onEvent, onSignInWithGoogleClick = onSignInWithGoogleClick)
    }
}

@Composable
private fun WelcomeMessage() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Hi!", style = MaterialTheme.typography.displayLarge)
        Text(text = "Create a new account", style = MaterialTheme.typography.displaySmall)
    }
}

@Composable
private fun InputSection(
    onEvent: (SignUpEvent) -> Unit,
    nameValue: String,
    emailValue: String,
    passwordValue: String,
    confirmPasswordValue: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ChatAppOutlinedTextField(
            value = nameValue,
            onValueChange = { onEvent(SignUpEvent.OnNameChanged(it)) },
            label = {
                Text(text = stringResource(id = AppStrings.username))
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Person, contentDescription = null)
            }
        )
        AuthEmailOutlinedTextField(
            value = emailValue,
            onValueChange = { onEvent(SignUpEvent.OnEmailChanged(it)) },
            labelText = stringResource(id = AppStrings.email),
            modifier = Modifier.fillMaxWidth()
        )
        AuthPasswordOutlinedTextField(
            value = passwordValue,
            onValueChange = { onEvent(SignUpEvent.OnPasswordChange(it)) },
            labelText = stringResource(id = AppStrings.password),
            modifier = Modifier.fillMaxWidth()
        )
        AuthPasswordOutlinedTextField(
            value = confirmPasswordValue,
            onValueChange = { onEvent(SignUpEvent.OnConfirmPasswordChange(it)) },
            labelText = stringResource(id = AppStrings.confirm_password),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SubmitSignUpSection(
    onEvent: (SignUpEvent) -> Unit,
    onSignInWithGoogleClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChatAppButton(
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
        ChatAppImageButton(
            onClick = onSignInWithGoogleClick,
            image = AppResources.ic_google
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account?")
            ChatAppTextButton(
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
        emailValue = "",
        passwordValue = "",
        confirmPasswordValue = "",
        onSignInWithGoogleClick = {}
    )
}