package com.ahmetocak.user_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
internal fun UserDetailRoute(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    viewModel: UserDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: UserDetailUiEvent -> viewModel.onEvent(event) }
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
                title = {},
                navigationIcon = {
                    ChatAppIconButton(
                        onClick = { onEvent(UserDetailUiEvent.OnBackClick) },
                        imageVector = ChatAppIcons.Default.arrowBack
                    )
                }
            )
        }
    ) { paddingValues ->
        UserDetailScreen(
            userImageUrl = uiState.userImage,
            username = uiState.username,
            userEmail = uiState.userEmail,
            onEvent = onEvent
        )
    }
}

@Composable
internal fun UserDetailScreen(
    modifier: Modifier = Modifier,
    userImageUrl: String?,
    username: String,
    userEmail: String,
    onEvent: (UserDetailUiEvent) -> Unit
) {
    val width = LocalConfiguration.current.screenWidthDp.dp
    val profileImageModifier = Modifier
        .size(width / 4)
        .clip(CircleShape)
        .clickable(onClick = { onEvent(UserDetailUiEvent.OnImageClick) })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            userImageUrl?.let { url ->
                AnimatedNetworkImage(
                    modifier = profileImageModifier,
                    imageUrl = url
                )
            } ?: run {
                Image(
                    modifier = profileImageModifier,
                    painter = painterResource(id = AppResources.blank_profile),
                    contentDescription = null
                )
            }
            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = userEmail,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            ActionCard(
                actionIcon = ChatAppIcons.Outlined.call,
                onClick = { onEvent(UserDetailUiEvent.OnVoiceCallClick) },
                actionTitle = "Voice Call"
            )
            ActionCard(
                actionIcon = ChatAppIcons.Outlined.video,
                onClick = { onEvent(UserDetailUiEvent.OnVideoCallClick) },
                actionTitle = "Video Call"
            )
        }
    }
}

@Composable
private fun ActionCard(actionIcon: ImageVector, onClick: () -> Unit, actionTitle: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = actionIcon, contentDescription = null)
                Text(text = actionTitle)
            }
        }
    }
}

@Preview
@Composable
fun PreviewUserDetailScreen() {
    Surface {
        ChatAppTheme {
            UserDetailScreen(
                userImageUrl = null,
                username = "Ahmet Ocak",
                userEmail = "ocak6139@gmail.com",
                onEvent = {}
            )
        }
    }
}