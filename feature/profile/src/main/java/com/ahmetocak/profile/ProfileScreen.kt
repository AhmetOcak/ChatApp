package com.ahmetocak.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.components.ChatAppModalBottomSheet
import com.ahmetocak.designsystem.components.ChatAppScaffold
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.LoadingState
import com.ahmetocak.ui.EditableImage
import com.ahmetocak.designsystem.R.dimen as ChatAppDimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    upPress: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: ProfileUiEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(action: () -> Unit) {
            action()
            viewModel.resetNavigation()
        }

        when (navigationState) {
            NavigationState.Back -> performNavigation { upPress() }
            NavigationState.None -> Unit
        }
    }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onEvent(ProfileUiEvent.OnUploadImageClick(uri))
        }
    }

    if (uiState.showUpdateUserNameSheet) {
        ChatAppModalBottomSheet(
            title = "Update Username",
            onDismissRequest = remember { { onEvent(ProfileUiEvent.OnDismissUpdateUsernameSheet) } },
            value = uiState.value,
            onValueChange = remember { { onEvent(ProfileUiEvent.OnValueChange(it)) } },
            onSubmitClick = remember { { onEvent(ProfileUiEvent.OnUpdateUserNameClick) } },
            isLoading = uiState.loadingState == LoadingState.Loading
        )
    }

    ChatAppScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.profile)) },
                navigationIcon = {
                    ChatAppIconButton(
                        onClick = remember { { onEvent(ProfileUiEvent.OnBackClick) } },
                        imageVector = ChatAppIcons.Default.arrowBack
                    )
                }
            )
        }
    ) { paddingValues ->
        ProfileScreen(
            modifier = modifier.padding(paddingValues),
            userImageUrl = uiState.userImageUrl,
            username = uiState.username,
            email = uiState.userEmail,
            onEvent = onEvent,
            onPickImageClick = remember {
                {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            },
            isImageUploading = uiState.imageUploadingState == LoadingState.Loading
        )
    }
}

@Composable
internal fun ProfileScreen(
    modifier: Modifier = Modifier,
    userImageUrl: String?,
    username: String,
    email: String,
    onEvent: (ProfileUiEvent) -> Unit,
    onPickImageClick: () -> Unit,
    isImageUploading: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = dimensionResource(id = ChatAppDimen.padding_16)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_16)))
        EditableImage(
            imageUrl = userImageUrl,
            onPickImageClick = onPickImageClick,
            isImageUploading = isImageUploading
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_32)))
        UserNameSection(
            information = username,
            onClick = remember { { onEvent(ProfileUiEvent.OnShowUpdateUsernameSheet) } }
        )
        UserEmailSection(information = email)
    }
}

@Composable
private fun UserNameSection(information: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = ChatAppDimen.padding_16),
                    horizontal = dimensionResource(id = ChatAppDimen.padding_32)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ChatAppIcons.Filled.person,
                    contentDescription = null
                )
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = stringResource(id = R.string.username),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(text = information, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Icon(
                imageVector = ChatAppIcons.Filled.edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun UserEmailSection(information: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.Transparent)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = dimensionResource(id = ChatAppDimen.padding_16),
                    horizontal = dimensionResource(id = ChatAppDimen.padding_32)
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ChatAppIcons.Filled.email,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = ChatAppDimen.padding_16)))
            Column {
                Text(
                    text = stringResource(id = R.string.email),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(text = information, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Preview
@Composable
fun PreviewProfileScreen() {
    Surface {
        ChatAppTheme {
            ProfileScreen(
                userImageUrl = null,
                username = "Ahmet",
                email = "ocak6139@gmail.com",
                onEvent = {},
                onPickImageClick = {},
                isImageUploading = false
            )
        }
    }
}