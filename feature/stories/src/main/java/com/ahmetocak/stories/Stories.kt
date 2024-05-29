package com.ahmetocak.stories

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.Story
import com.ahmetocak.ui.StoryItem
import com.ahmetocak.designsystem.R.drawable as AppResources

@Composable
internal fun StoriesRoute(
    onNavigateToUserDetail: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoriesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: StoriesUiEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(action: () -> Unit) {
            action()
            viewModel.resetNavigation()
        }

        when (val state = navigationState) {
            is NavigationState.UserDetail -> performNavigation { onNavigateToUserDetail(state.id) }
            is NavigationState.None -> Unit
        }
    }

    when (uiState.currentScreen) {
        is CurrentScreen.Stories -> {
            Stories(
                modifier = modifier,
                onEvent = onEvent,
                currentUserImg = uiState.currentUserImg,
                storyList = uiState.storyList
            )
        }

        is CurrentScreen.AddStory -> {
            // TODO: Add Story
        }

        is CurrentScreen.LookStory -> {
            // TODO: Look Story
        }
    }
}

@Composable
internal fun Stories(
    modifier: Modifier = Modifier,
    onEvent: (StoriesUiEvent) -> Unit,
    storyList: List<Story>,
    currentUserImg: String?
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = "Status",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            MyStatus(
                imageUrl = currentUserImg,
                onClick = { onEvent(StoriesUiEvent.OnAddStoryClick) })
        }
        Text(
            text = "Last updates",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )
        LazyColumn(
            modifier = Modifier.weight(3f),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(storyList, key = { it.id }) { story ->
                StoryItem(
                    userId = story.id,
                    username = story.username,
                    storyDate = story.storyDate,
                    storyImageUrl = story.imageUrl,
                    onClick = { onEvent(StoriesUiEvent.OnStoryClick(story.id)) }
                )
            }
        }
    }
}

@Composable
private fun MyStatus(imageUrl: String?, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.BottomEnd) {
            imageUrl?.let { img ->
                AnimatedNetworkImage(imageUrl = img)
            } ?: run {
                Image(
                    painter = painterResource(id = AppResources.blank_profile),
                    contentDescription = null
                )
            }
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = "My Status",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap to add a status update",
                style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray)
            )
        }
    }
}

@Preview
@Composable
fun StoriesScreenPreview() {
    ChatAppTheme {
        Surface {
            Stories(currentUserImg = null, storyList = emptyList(), onEvent = {})
        }
    }
}