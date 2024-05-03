package com.ahmetocak.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.components.ChatAppScaffold
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.theme.ChatAppTheme
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
            email = uiState.userEmail
        )
    }
}

@Composable
internal fun ProfileScreen(
    modifier: Modifier = Modifier,
    userImageUrl: String?,
    username: String,
    email: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        EditableUserProfileImage(imageUrl = userImageUrl)
        Spacer(modifier = Modifier.height(32.dp))
        UserDetailItem(
            title = "Username",
            information = username,
            icon = ChatAppIcons.Outlined.person,
            onClick = {}
        )
        UserDetailItem(
            title = "Email",
            information = email,
            icon = ChatAppIcons.Outlined.person,
            onClick = {}
        )
    }
}

@Composable
private fun UserDetailItem(
    title: String,
    information: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.weight(1f))
            Column(modifier = Modifier.weight(5f), horizontalAlignment = Alignment.Start) {
                Text(text = title, style = MaterialTheme.typography.titleSmall)
                Text(text = information, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(
                imageVector = ChatAppIcons.Outlined.edit,
                contentDescription = null,
                modifier = Modifier.weight(1f),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun EditableUserProfileImage(imageUrl: String?) {
    val size = (LocalConfiguration.current.screenWidthDp / 2.5f).dp

    Box(
        modifier = Modifier
            .size(size)
            .clickable(onClick = {  })
    ) {
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
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            ChatAppIconButton(
                onClick = { /*TODO*/ },
                imageVector = ChatAppIcons.Outlined.camera,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
            )
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
                username = "Ahmet Ocak",
                email = "ocak6139@gmail.com",
            )
        }
    }
}