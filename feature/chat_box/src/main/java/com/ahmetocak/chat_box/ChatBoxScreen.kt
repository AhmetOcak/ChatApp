package com.ahmetocak.chat_box

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.ahmetocak.model.Message
import com.ahmetocak.ui.ComingChatBubbleItem
import com.ahmetocak.ui.OngoingChatBubbleItem
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
                onEvent = onEvent
            )
        }
    ) { paddingValues ->
        ChatBoxScreen(
            modifier = Modifier.padding(paddingValues),
            messageList = PreviewMessageList
        )
    }
}

@Composable
internal fun ChatBoxScreen(modifier: Modifier = Modifier, messageList: List<Message>) {
    // TODO: This is for a test. Remove when you integrate the business data
    val myId = "bob456"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        itemsIndexed(messageList) { index, chat ->
            val isConsecutiveMessage = index > 0 && messageList[index - 1].authorId == chat.authorId

            if (!isConsecutiveMessage) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (chat.isComingFromMe(myId)) {
                OngoingChatBubbleItem(
                    author = chat.author,
                    message = chat.message,
                    time = chat.time,
                    seenByList = listOf(null, null),
                    isAuthorSame = isConsecutiveMessage
                )
            } else {
                ComingChatBubbleItem(
                    author = chat.author,
                    message = chat.message,
                    time = chat.time,
                    authorImgUrl = chat.authorImage,
                    isAuthorSame = isConsecutiveMessage
                )
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
                onClick = { onEvent(ChatBoxUiEvent.OnCameraClick) },
                imageVector = ChatAppIcons.Outlined.camera
            )
            ChatAppIconButton(
                onClick = { onEvent(ChatBoxUiEvent.OnCallClick) },
                imageVector = ChatAppIcons.Outlined.call
            )
            ChatAppIconButton(
                onClick = { onEvent(ChatBoxUiEvent.OnMenuClick) },
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

@Composable
private fun ChatBoxBottomBar(messageValue: String, onEvent: (ChatBoxUiEvent) -> Unit) {
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
                    onValueChange = { onEvent(ChatBoxUiEvent.OnMessageValueChange(it)) },
                    singleLine = true,
                    trailingIcon = {
                        Row {
                            ChatAppIconButton(
                                onClick = { onEvent(ChatBoxUiEvent.OnAttachDocClick) },
                                imageVector = ChatAppIcons.Filled.attach,
                            )
                            AnimatedVisibility(visible = messageValue.isBlank()) {
                                ChatAppIconButton(
                                    onClick = { onEvent(ChatBoxUiEvent.OnCameraClick) },
                                    imageVector = ChatAppIcons.Outlined.camera
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(text = "Message")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }
        IconButton(
            onClick = {
                if (messageValue.isNotBlank()) {
                    onEvent(ChatBoxUiEvent.OnMicrophonePress)
                } else {
                    onEvent(ChatBoxUiEvent.OnSendMessageClick)
                }
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
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
            AnimatedVisibility(visible = messageValue.isBlank()) {
                Icon(
                    imageVector = ChatAppIcons.Filled.microphone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

private val BottomBarHeight = 56.dp


private val AliceImgUrl =
    "https://fastly.picsum.photos/id/170/200/200.jpg?hmac=2Xh3j3MMZE07_G7UDPgPRm557LRHzyFrkyeWRXdhdvU"
private val BobImgUrl =
    "https://fastly.picsum.photos/id/110/200/200.jpg?hmac=aekmsQTsPRt4hCd1khMC5QVihAaBeTigUCpcDBzhXlY"
private val PreviewMessageList = listOf(
    Message("alice123", "Alice", AliceImgUrl, "Hello Bob, how are you?", "10:00"),
    Message("alice123", "Alice", AliceImgUrl, "I'm fine, thank you.", "10:05"),
    Message("bob456", "Bob", BobImgUrl, "Hi Alice, I'm good, how about you?", "10:10"),
    Message("alice123", "Alice", AliceImgUrl, "What are you doing?", "10:15"),
    Message("bob456", "Bob", BobImgUrl, "I'm just reading a book. You?", "10:20"),
    Message("bob456", "Bob", BobImgUrl, "Only reading a book. You?", "10:25"),
    Message("alice123", "Alice", AliceImgUrl, "I'm watching a movie.", "10:30"),
    Message("bob456", "Bob", BobImgUrl, "Enjoy your movie!", "10:35"),
    Message("alice123", "Alice", AliceImgUrl, "Thank you.", "10:40"),
    Message("bob456", "Bob", BobImgUrl, "You're welcome.", "10:45"),
    Message("bob456", "Bob", BobImgUrl, "See you next time, goodbye!", "10:50"),
    Message("alice123", "Alice", AliceImgUrl, "Goodbye.", "10:55"),
    Message("alice123", "Alice", AliceImgUrl, "See you later.", "11:00"),
    Message("bob456", "Bob", BobImgUrl, "Later!", "11:05")
)


@Preview
@Composable
fun ChatBoxPreview() {
    Surface {
        ChatAppTheme {
            ChatBoxScreen(messageList = PreviewMessageList)
        }
    }
}

@Preview
@Composable
fun ChatBoxTopBarPreview() {
    Surface {
        ChatAppTheme {
            ChatBoxTopBar(
                chatBoxTitle = "Title",
                members = "member1, member2, member3, member4, member5",
                imageUrl = null,
                onEvent = {}
            )
        }
    }
}

@Preview
@Composable
fun ChatBoxBottomBarPreview() {
    Surface {
        ChatAppTheme {
            ChatBoxBottomBar(messageValue = "", onEvent = {})
        }
    }
}