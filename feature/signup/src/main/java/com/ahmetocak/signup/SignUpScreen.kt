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
import com.ahmetocak.designsystem.components.ChatAppOutlinedTextField
import com.ahmetocak.designsystem.components.ChatAppButton
import com.ahmetocak.designsystem.components.ChatAppImageButton
import com.ahmetocak.designsystem.components.ChatAppTextButton
import com.ahmetocak.designsystem.components.auth.AuthEmailOutlinedTextField
import com.ahmetocak.designsystem.components.auth.AuthPasswordOutlinedTextField
import com.ahmetocak.model.LoadingState
import com.ahmetocak.designsystem.R.drawable as AppResources
import com.ahmetocak.designsystem.R.string as AppStrings
import com.ahmetocak.designsystem.R.dimen as ChatAppDimen

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
        onEvent = remember { onEvent },
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
            .padding(horizontal = dimensionResource(id = ChatAppDimen.padding_16)),
        verticalArrangement = Arrangement.Center
    ) {
        WelcomeMessage()
        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_32)))
        InputSection(
            onEvent = onEvent,
            nameValue = nameValue,
            emailValue = emailValue,
            passwordValue = passwordValue,
            confirmPasswordValue = confirmPasswordValue,
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_32)))
        SubmitSignUpSection(onEvent = onEvent, onSignInWithGoogleClick = onSignInWithGoogleClick)
    }
}

@Composable
private fun WelcomeMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_8))
    ) {
        Text(text = stringResource(id = R.string.hi), style = MaterialTheme.typography.displayLarge)
        Text(
            text = stringResource(id = R.string.create_new_account),
            style = MaterialTheme.typography.displaySmall
        )
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
    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_8))) {
        ChatAppOutlinedTextField(
            value = nameValue,
            onValueChange = remember { { onEvent(SignUpEvent.OnNameChanged(it)) } },
            labelText = stringResource(id = AppStrings.username),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = Icons.Filled.Person
        )
        AuthEmailOutlinedTextField(
            value = emailValue,
            onValueChange = remember { { onEvent(SignUpEvent.OnEmailChanged(it)) } },
            labelText = stringResource(id = AppStrings.email),
            modifier = Modifier.fillMaxWidth()
        )
        AuthPasswordOutlinedTextField(
            value = passwordValue,
            onValueChange = remember { { onEvent(SignUpEvent.OnPasswordChange(it)) } },
            labelText = stringResource(id = AppStrings.password),
            modifier = Modifier.fillMaxWidth()
        )
        AuthPasswordOutlinedTextField(
            value = confirmPasswordValue,
            onValueChange = remember { { onEvent(SignUpEvent.OnConfirmPasswordChange(it)) } },
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
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_8))
    ) {
        ChatAppButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = remember { { onEvent(SignUpEvent.OnSignUpClick) } },
            text = stringResource(id = R.string.sign_up)
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
            Text(text = stringResource(id = R.string.already_have_account))
            ChatAppTextButton(
                onClick = remember { { onEvent(SignUpEvent.OnAlreadyHaveAccountClick) } },
                text = stringResource(id = R.string.sign_in)
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