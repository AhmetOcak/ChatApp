package com.ahmetocak.settings

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.components.ChatAppSubmitDialog
import com.ahmetocak.designsystem.components.ChatAppSubmitValueDialog
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.domain.usecase.auth.SignInProvider
import com.ahmetocak.settings.components.SettingItem
import com.ahmetocak.designsystem.R.dimen as ChatAppDimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsRoute(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: SettingUiEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(onAction: () -> Unit) {
            onAction()
            viewModel.resetNavigation()
        }
        when (navigationState) {
            NavigationState.None -> {}
            NavigationState.Back -> performNavigation(upPress)
            NavigationState.Login -> performNavigation(onNavigateLogin)
            NavigationState.Profile -> performNavigation(onNavigateToProfile)
        }
    }

    if (uiState.showDeleteAccountDialog) {
        DeleteAccountDialog(
            onEvent = onEvent,
            isLoading = uiState.isLoading,
            passwordValue = uiState.password,
            currentSignInProvider = viewModel.currentSignInProvider
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    ChatAppIconButton(
                        onClick = upPress,
                        imageVector = ChatAppIcons.Default.arrowBack
                    )
                }
            )
        }
    ) { paddingValues ->
        SettingScreen(
            modifier = Modifier.padding(paddingValues),
            onEvent = onEvent,
            darkMode = uiState.isDarkMode,
            dynamicColor = uiState.isDynamicColor
        )
    }
}

@Composable
internal fun SettingScreen(
    modifier: Modifier,
    onEvent: (SettingUiEvent) -> Unit,
    darkMode: Boolean,
    dynamicColor: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = dimensionResource(id = ChatAppDimen.padding_8),
                horizontal = dimensionResource(id = ChatAppDimen.padding_16)
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_8))
    ) {
        SettingItem(
            nameId = Settings.PROFILE.nameId,
            icon = Settings.PROFILE.icon,
            onClick = remember { { onEvent(SettingUiEvent.OnProfileClick) } },
        )
        SettingItem(
            nameId = Settings.DARK_THEME.nameId,
            icon = Settings.DARK_THEME.icon,
            checked = darkMode,
            onCheckedChange = remember { { onEvent(SettingUiEvent.OnDarkModeSwitched(it)) } }
        )
        if (Build.VERSION.SDK_INT >= 31) {
            SettingItem(
                nameId = Settings.DYNAMIC_COLOR.nameId,
                icon = Settings.DYNAMIC_COLOR.icon,
                checked = dynamicColor,
                onCheckedChange = remember { { onEvent(SettingUiEvent.OnDynamicColorSwitched(it)) } }
            )
        }
        SettingItem(
            nameId = Settings.DELETE_ACCOUNT.nameId,
            icon = Settings.DELETE_ACCOUNT.icon,
            onClick = remember { { onEvent(SettingUiEvent.OnStartDeleteAccountDialogClick) } },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        )
        SettingItem(
            nameId = Settings.SIGN_OUT.nameId,
            icon = Settings.SIGN_OUT.icon,
            onClick = remember { { onEvent(SettingUiEvent.OnSignOutClick) } }
        )
    }
}

@Composable
private fun DeleteAccountDialog(
    onEvent: (SettingUiEvent) -> Unit,
    isLoading: Boolean,
    passwordValue: String,
    currentSignInProvider: SignInProvider
) {
    when (currentSignInProvider) {
        SignInProvider.GOOGLE -> {
            ChatAppSubmitDialog(
                onDismissRequest = remember { { onEvent(SettingUiEvent.OnDismissDeleteAccountDialog) } },
                onSubmitClick = remember { { onEvent(SettingUiEvent.OnSubmitDeleteAccountClick) } },
                title = stringResource(id = R.string.delete_account),
                description = stringResource(id = R.string.delete_account_descr),
                submitText = stringResource(id = R.string.delete)
            )
        }

        SignInProvider.EMAIL_PASSWORD -> {
            ChatAppSubmitValueDialog(
                onDismissRequest = remember { { onEvent(SettingUiEvent.OnDismissDeleteAccountDialog) } },
                onSubmitClick = remember { { onEvent(SettingUiEvent.OnSubmitDeleteAccountClick) } },
                title = stringResource(id = R.string.delete_account),
                description = stringResource(id = R.string.delete_account_descr),
                submitText = stringResource(id = R.string.delete),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = remember { { onEvent(SettingUiEvent.OnSubmitDeleteAccountClick) } }),
                value = passwordValue,
                onValueChange = remember { { onEvent(SettingUiEvent.OnPasswordValueChange(it)) } },
                isLoading = isLoading,
                visualTransformation = PasswordVisualTransformation()
            )
        }
    }
}

enum class Settings(val nameId: Int, val icon: ImageVector) {
    PROFILE(R.string.profile, ChatAppIcons.Filled.account),
    DARK_THEME(R.string.dark_mode, ChatAppIcons.Filled.darkMode),
    DYNAMIC_COLOR(R.string.dynamic_color, ChatAppIcons.Filled.brightnessAuto),
    DELETE_ACCOUNT(R.string.delete_account, ChatAppIcons.Filled.delete),
    SIGN_OUT(R.string.sign_out, ChatAppIcons.Default.logout)
}