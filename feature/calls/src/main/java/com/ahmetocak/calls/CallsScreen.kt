package com.ahmetocak.calls

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.ChatAppProgressIndicator
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.Call
import com.ahmetocak.model.CallDirection
import com.ahmetocak.model.CallType
import com.ahmetocak.model.LoadingState
import com.ahmetocak.ui.CallItem

@Composable
internal fun CallsRoute(
    onNavigateCallInfo: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CallsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: CallsUiEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(action: () -> Unit) {
            action()
            viewModel.resetNavigation()
        }

        when (val state = navigationState) {
            is NavigationState.CallDetail -> performNavigation { onNavigateCallInfo(state.id) }
            is NavigationState.None -> Unit
        }
    }

    ChatAppProgressIndicator(visible = uiState.loadingState is LoadingState.Loading)

    CallsScreen(modifier = modifier, callList = uiState.callList, onEvent = onEvent)
}

@Composable
internal fun CallsScreen(
    modifier: Modifier = Modifier,
    callList: List<Call>,
    onEvent: (CallsUiEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(callList, key = { it.id }) { call ->
            CallItem(
                id = call.id,
                imageUrl = call.imageUrl,
                title = call.title,
                date = call.date,
                onClick = { onEvent(CallsUiEvent.OnCallClick(call.id)) },
                onLongClick = { onEvent(CallsUiEvent.OnCallLongClick(call.id)) },
                onImageClick = { onEvent(CallsUiEvent.OnCallImageClick(call.id)) },
                callDirectionIcon = {
                    when (call.callDirection) {
                        CallDirection.COMING -> {
                            Icon(
                                modifier = Modifier.rotate(135f),
                                imageVector = Icons.AutoMirrored.Default.ArrowRightAlt,
                                contentDescription = null,
                                tint = if (call.isCallSuccessful) Color.Green else Color.Red
                            )
                        }

                        CallDirection.ONGOING -> {
                            Icon(
                                modifier = Modifier.rotate(-45f),
                                imageVector = Icons.AutoMirrored.Default.ArrowRightAlt,
                                contentDescription = null,
                                tint = if (call.isCallSuccessful) Color.Green else Color.Red
                            )
                        }
                    }
                },
                callTypeIcon = when (call.callType) {
                    CallType.VOICE -> Icons.Outlined.Call
                    CallType.VIDEO -> Icons.Outlined.Videocam
                },
                isCallSuccessful = call.isCallSuccessful
            )
        }
    }
}

@Preview
@Composable
fun CallsScreenPreview() {
    Surface {
        ChatAppTheme {
            CallsScreen(callList = emptyList(), onEvent = {})
        }
    }
}