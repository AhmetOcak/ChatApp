package com.ahmetocak.chat_box

import android.Manifest
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ahmetocak.common.ext.showMessageTime
import com.ahmetocak.designsystem.components.AnimatedNetworkImage
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.components.ChatAppScaffold
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.model.Message
import com.ahmetocak.model.MessageType
import com.ahmetocak.ui.ComingChatBubbleAudioItem
import com.ahmetocak.ui.ComingChatBubbleItem
import com.ahmetocak.ui.OngoingChatBubbleAudioItem
import com.ahmetocak.ui.OngoingChatBubbleItem
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ahmetocak.designsystem.R.drawable as AppResources

@Composable
internal fun ChatBoxRoute(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    navigateChatDetail: (String) -> Unit,
    navigateChatDocs: (String) -> Unit,
    navigateCamera: () -> Unit,
    viewModel: ChatBoxViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: ChatBoxUiEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(onAction: () -> Unit) {
            onAction()
            viewModel.resetNavigation()
        }

        when (val state = navigationState) {
            is NavigationState.Back -> performNavigation { upPress() }
            is NavigationState.ChatDetail -> performNavigation { navigateChatDetail(state.id) }
            is NavigationState.ChatDocuments -> performNavigation { navigateChatDocs(state.id) }
            is NavigationState.Camera -> performNavigation { navigateCamera() }
            is NavigationState.None -> Unit
        }
    }

    val messageList = uiState.messageList.collectAsLazyPagingItems()

    ChatAppScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ChatBoxTopBar(
                chatBoxTitle = uiState.title,
                members = uiState.members,
                imageUrl = uiState.imageUrl,
                onEvent = onEvent
            )
        },
        bottomBar = {
            ChatBoxBottomBar(
                messageValue = uiState.messageValue,
                onEvent = onEvent,
                isAudioRecording = uiState.audioRecordStatus == AudioRecordStatus.RECORDING
            )
        }
    ) { paddingValues ->
        ChatBoxScreen(
            modifier = Modifier.padding(paddingValues),
            messageList = messageList,
            currentUserEmail = uiState.currentUser?.email ?: "",
            onPlayClick = remember { { onEvent(ChatBoxUiEvent.OnPlayAudioClick(it)) }},
            isAudioPlaying = uiState.audioPlayStatus == AudioPlayStatus.PLAYING
        )
    }
}

@Composable
internal fun ChatBoxScreen(
    modifier: Modifier = Modifier,
    messageList: LazyPagingItems<Message>,
    currentUserEmail: String,
    onPlayClick: (Uri) -> Unit,
    isAudioPlaying: Boolean
) {
    var selectedIndex by remember { mutableStateOf(-1) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        reverseLayout = true
    ) {
        items(messageList.itemCount) { index ->
            messageList[index]?.let { message ->
                when (message.messageType) {
                    MessageType.TEXT -> {
                        if (message.isComingFromMe(currentUserEmail)) {
                            OngoingChatBubbleItem(
                                author = message.senderUsername,
                                message = message.messageContent,
                                time = message.sentAt.showMessageTime()
                            )
                        } else {
                            ComingChatBubbleItem(
                                author = message.senderUsername,
                                message = message.messageContent,
                                time = message.sentAt.showMessageTime(),
                                authorImgUrl = message.senderImgUrl
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    MessageType.AUDIO -> {
                        if (message.isComingFromMe(currentUserEmail)) {
                            OngoingChatBubbleAudioItem(
                                author = message.senderUsername,
                                time = message.sentAt.showMessageTime(),
                                audioUrl = message.messageContent.toUri(),
                                isAudioPlaying = isAudioPlaying && selectedIndex == index,
                                onPlayClick = remember {
                                    {
                                        selectedIndex = index
                                        onPlayClick(it)
                                    }
                                }
                            )
                        } else {
                            ComingChatBubbleAudioItem(
                                author = message.senderUsername,
                                time = message.sentAt.showMessageTime(),
                                audioUrl = message.messageContent.toUri(),
                                authorImgUrl = message.senderImgUrl,
                                isAudioPlaying = isAudioPlaying && selectedIndex == index,
                                onPlayClick = remember {
                                    {
                                        selectedIndex = index
                                        onPlayClick(it)
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatBoxTopBar(
    chatBoxTitle: String,
    members: String,
    imageUrl: String?,
    onEvent: (ChatBoxUiEvent) -> Unit
) {
    TopAppBar(
        title = {
            if (members.isNotBlank()) {
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = chatBoxTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = members,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            } else {
                Text(text = chatBoxTitle, style = MaterialTheme.typography.titleMedium)
            }
        },
        actions = {
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnCameraClick) } },
                imageVector = ChatAppIcons.Outlined.camera
            )
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnCallClick) } },
                imageVector = ChatAppIcons.Outlined.call
            )
            ChatAppIconButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnMenuClick) } },
                imageVector = ChatAppIcons.Filled.moreVert
            )
        },
        navigationIcon = {
            Surface(
                onClick = { onEvent(ChatBoxUiEvent.OnBackClick) },
                shape = RoundedCornerShape(20)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = ChatAppIcons.Default.arrowBack, contentDescription = null)
                    imageUrl?.let { url ->
                        AnimatedNetworkImage(
                            imageUrl = url,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    } ?: run {
                        Icon(
                            painter = painterResource(id = AppResources.blank_profile),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ChatBoxBottomBar(
    messageValue: String,
    onEvent: (ChatBoxUiEvent) -> Unit,
    isAudioRecording: Boolean
) {
    val microphonePermissionState = rememberPermissionState(
        permission = Manifest.permission.RECORD_AUDIO
    )

    val context = LocalContext.current
    val infiniteTransition = rememberInfiniteTransition(label = "recording text")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFFFF3131),
        targetValue = Color(0xFFC41E3A),
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "color"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .padding(bottom = 8.dp)
            .height(BottomBarHeight)
    ) {
        Surface(modifier = Modifier.weight(5f), shape = CircleShape) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.fillMaxSize(),
                    value = messageValue,
                    onValueChange = remember { { onEvent(ChatBoxUiEvent.OnMessageValueChange(it)) } },
                    singleLine = true,
                    trailingIcon = {
                        Row {
                            ChatAppIconButton(
                                onClick = remember { { onEvent(ChatBoxUiEvent.OnAttachDocClick) } },
                                imageVector = ChatAppIcons.Filled.attach,
                            )
                            AnimatedVisibility(visible = messageValue.isBlank()) {
                                ChatAppIconButton(
                                    onClick = remember { { onEvent(ChatBoxUiEvent.OnCameraClick) } },
                                    imageVector = ChatAppIcons.Outlined.camera
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = if (isAudioRecording) "Recording..." else "Message",
                            color = if (isAudioRecording) animatedColor else Color.Unspecified
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    enabled = !isAudioRecording
                )
            }
        }
        IconButton(
            onClick = {
                if (messageValue.isBlank()) {
                    onEvent(
                        ChatBoxUiEvent.OnMicrophonePress(context = context) {
                            return@OnMicrophonePress if (microphonePermissionState.status.isGranted) {
                                true
                            } else {
                                microphonePermissionState.launchPermissionRequest()
                                false
                            }
                        }
                    )
                } else {
                    onEvent(ChatBoxUiEvent.OnSendMessageClick)
                }
            },
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
                .padding(8.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            AnimatedVisibility(visible = messageValue.isNotBlank()) {
                Icon(
                    imageVector = ChatAppIcons.Default.send,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
            AnimatedVisibility(visible = messageValue.isBlank() && !isAudioRecording) {
                Icon(
                    imageVector = ChatAppIcons.Filled.microphone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
            AnimatedVisibility(visible = messageValue.isBlank() && isAudioRecording) {
                Icon(
                    imageVector = ChatAppIcons.Filled.stop,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

private val BottomBarHeight = 56.dp