package com.ahmetocak.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.components.ChatAppModalBottomSheet
import com.ahmetocak.designsystem.components.ChatAppScaffold
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.LoadingState
import com.ahmetocak.designsystem.R.drawable as AppResources

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
            onDismissRequest = { onEvent(ProfileUiEvent.OnDismissUpdateUsernameSheet) },
            value = uiState.value,
            onValueChange = { onEvent(ProfileUiEvent.OnValueChange(it)) },
            onCancelClick = { onEvent(ProfileUiEvent.OnDismissUpdateUsernameSheet) },
            onSubmitClick = { onEvent(ProfileUiEvent.OnUpdateUserNameClick) },
            isLoading = uiState.loadingState == LoadingState.Loading
        )
    }

    ChatAppScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile") },
                navigationIcon = {
                    ChatAppIconButton(
                        onClick = { onEvent(ProfileUiEvent.OnBackClick) },
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
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        EditableUserProfileImage(
            imageUrl = userImageUrl,
            onPickImageClick = onPickImageClick,
            isImageUploading = isImageUploading
        )
        Spacer(modifier = Modifier.height(32.dp))
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
                .padding(vertical = 16.dp, horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ChatAppIcons.Filled.person,
                    contentDescription = null
                )
                Column(horizontalAlignment = Alignment.Start) {
                    Text(text = "Username", style = MaterialTheme.typography.titleSmall)
                    Text(text = information, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Icon(
                imageVector = ChatAppIcons.Outlined.edit,
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
                .padding(vertical = 16.dp, horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ChatAppIcons.Filled.email,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "Email", style = MaterialTheme.typography.titleSmall)
                Text(text = information, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun EditableUserProfileImage(
    imageUrl: String?,
    onPickImageClick: () -> Unit,
    isImageUploading: Boolean
) {
    val size = (LocalConfiguration.current.screenWidthDp / 2.5f).dp

    Box(
        modifier = Modifier
            .size(size)
            .clickable(onClick = { }),
        contentAlignment = Alignment.Center
    ) {
        if (isImageUploading) {
            CircularProgressIndicator()
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            ) {
                imageUrl?.let { url ->
                    AnimatedNetworkImage(imageUrl = url, modifier = Modifier.fillMaxSize())
                } ?: run {
                    Image(
                        painter = painterResource(id = AppResources.blank_profile),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize().aspectRatio(1f)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                ChatAppIconButton(
                    onClick = onPickImageClick,
                    imageVector = ChatAppIcons.Outlined.camera,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                )
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