package com.ahmetocak.chat_box

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ahmetocak.chat_box.components.AddParticipantItem
import com.ahmetocak.chat_box.components.AttachItem
import com.ahmetocak.chat_box.components.BottomBarHeight
import com.ahmetocak.chat_box.components.ChatBoxBottomBar
import com.ahmetocak.chat_box.components.ChatBoxTopBar
import com.ahmetocak.chat_box.components.LeaveGroupItem
import com.ahmetocak.chat_box.components.MediaItem
import com.ahmetocak.common.ext.showMessageTime
import com.ahmetocak.designsystem.components.ChatAppButton
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.components.ChatAppOutlinedTextField
import com.ahmetocak.designsystem.components.ChatAppPlaceableProgressIndicator
import com.ahmetocak.designsystem.components.ChatAppProgressIndicator
import com.ahmetocak.designsystem.components.ChatAppScaffold
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.model.ChatGroupParticipants
import com.ahmetocak.model.LoadingState
import com.ahmetocak.model.Message
import com.ahmetocak.model.MessageType
import com.ahmetocak.ui.ChatItem
import com.ahmetocak.ui.EditableImage
import com.ahmetocak.ui.chat_bubble.ChatBubbleAudioItem
import com.ahmetocak.ui.chat_bubble.ChatBubbleImageItem
import com.ahmetocak.ui.chat_bubble.ChatBubblePdfItem
import com.ahmetocak.ui.chat_bubble.ChatBubbleTextItem
import com.ahmetocak.designsystem.R.dimen as ChatAppDimen

@Composable
internal fun ChatBoxRoute(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    navigateChatDetail: (String) -> Unit,
    navigateChatDocs: (String) -> Unit,
    navigateCamera: (Int, String, String, String?) -> Unit,
    viewModel: ChatBoxViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(newValue = { event: ChatBoxUiEvent ->
        viewModel.onEvent(event)
    })

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
            is NavigationState.Camera -> performNavigation {
                navigateCamera(
                    state.messageBoxId,
                    state.senderEmail,
                    state.senderUsername,
                    state.senderImgUrl
                )
            }

            is NavigationState.None -> Unit
        }
    }

    AnimatedVisibility(
        modifier = Modifier.zIndex(10f),
        visible = uiState.showAttachMenu,
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = slideOutVertically(targetOffsetY = { it / 4 })
    ) {
        val pickMediaLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                onEvent(ChatBoxUiEvent.OnSendImageClick(uri))
            }
            viewModel.resetAttachMenu()
        }

        val pickFileLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                onEvent(ChatBoxUiEvent.OnSendDocClick(uri))
            }
        }

        AttachSection(
            onAttachItemClick = remember {
                { type ->
                    when (type) {
                        AttachType.GALLERY -> {
                            pickMediaLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }

                        AttachType.DOCUMENT -> pickFileLauncher.launch("application/pdf")
                    }
                }
            }
        )
    }

    val messageList = uiState.messageList.collectAsLazyPagingItems()

    ChatAppScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ChatBoxTopBar(
                chatBoxTitle = uiState.title,
                imageUrl = uiState.imageUrl,
                onEvent = remember { onEvent },
                screenState = uiState.screenState
            )
        },
        bottomBar = {
            if (uiState.screenState is ScreenState.ChatBox) {
                ChatBoxBottomBar(
                    messageValue = uiState.messageValue,
                    onEvent = remember { onEvent },
                    isAudioRecording = uiState.audioRecordStatus == AudioRecordStatus.RECORDING
                )
            }
        }
    ) { paddingValues ->
        when (uiState.screenState) {
            is ScreenState.ChatBox -> {
                ChatBoxScreen(
                    modifier = Modifier.padding(paddingValues),
                    messageList = messageList,
                    currentUserEmail = uiState.currentUser?.email ?: "",
                    onPlayClick = remember { { onEvent(ChatBoxUiEvent.OnPlayAudioClick(it)) } },
                    isAudioPlaying = uiState.audioPlayStatus == AudioPlayStatus.PLAYING
                )
            }

            is ScreenState.GroupInfo -> {
                GroupInfoScreen(
                    modifier = Modifier.padding(paddingValues),
                    groupImgUrl = uiState.imageUrl,
                    groupName = uiState.title,
                    participantSize = viewModel.groupData.participants.size,
                    participants = viewModel.groupData.participants,
                    groupMediaMessages = uiState.mediaMessages,
                    isMediaMessagesLoading = uiState.loadingState == LoadingState.Loading,
                    onEvent = remember { onEvent }
                )
            }

            is ScreenState.GroupMedia -> {

            }

            is ScreenState.AddParticipant -> {
                AddParticipantScreen(
                    modifier = Modifier.padding(paddingValues),
                    onEvent = remember { onEvent },
                    participantVal = uiState.parEmailVal,
                    isLoading = uiState.loadingState == LoadingState.Loading
                )
            }
        }
    }
}

@Composable
private fun ChatBoxScreen(
    modifier: Modifier = Modifier,
    messageList: LazyPagingItems<Message>,
    currentUserEmail: String,
    onPlayClick: (Uri) -> Unit,
    isAudioPlaying: Boolean
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = ChatAppDimen.padding_8)),
        contentPadding = PaddingValues(vertical = dimensionResource(id = ChatAppDimen.padding_16)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_2)),
        reverseLayout = true
    ) {
        items(messageList.itemCount) { index ->
            messageList[index]?.let { message ->
                when (message.messageType) {
                    MessageType.TEXT -> {
                        ChatBubbleTextItem(
                            author = message.senderUsername,
                            message = message.messageContent,
                            time = message.sentAt.showMessageTime(),
                            authorImgUrl = message.senderImgUrl,
                            isComingFromMe = message.isComingFromMe(currentUserEmail)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_8)))
                    }

                    MessageType.AUDIO -> {
                        ChatBubbleAudioItem(
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
                            },
                            isComingFromMe = message.isComingFromMe(currentUserEmail)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_8)))
                    }

                    MessageType.IMAGE -> {
                        ChatBubbleImageItem(
                            author = message.senderUsername,
                            imageUrl = message.messageContent,
                            time = message.sentAt.showMessageTime(),
                            authorImgUrl = message.senderImgUrl,
                            isComingFromMe = message.isComingFromMe(currentUserEmail),
                            onClick = remember {
                                { viewImage(context = context, imageUrl = message.messageContent) }
                            }
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_8)))
                    }

                    MessageType.DOC -> {
                        ChatBubblePdfItem(
                            author = message.senderUsername,
                            pdfUrl = message.messageContent,
                            time = message.sentAt.showMessageTime(),
                            authorImgUrl = message.senderImgUrl,
                            onClick = remember { { viewPdf(context = context, uri = it) } },
                            isComingFromMe = message.isComingFromMe(currentUserEmail)
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_8)))
                    }
                }
            }
        }
    }
}

@Composable
private fun AttachSection(onAttachItemClick: (AttachType) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = ChatAppDimen.padding_32))
                .padding(bottom = BottomBarHeight + dimensionResource(id = ChatAppDimen.padding_16))
        ) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = ChatAppDimen.padding_32)),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = dimensionResource(id = ChatAppDimen.padding_16)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16))
            ) {
                items(attachList, key = { it.titleId }) {
                    AttachItem(
                        onClick = remember { { onAttachItemClick(it.attachType) } },
                        imageVector = it.icon,
                        title = stringResource(id = it.titleId)
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupInfoScreen(
    modifier: Modifier = Modifier,
    groupImgUrl: String?,
    groupName: String,
    participantSize: Int,
    groupMediaMessages: List<Message>,
    participants: List<ChatGroupParticipants>,
    isMediaMessagesLoading: Boolean,
    onEvent: (ChatBoxUiEvent) -> Unit
) {
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onEvent(ChatBoxUiEvent.UpdateGroupImage(uri))
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16))
        ) {
            EditableImage(
                imageUrl = groupImgUrl,
                onPickImageClick = remember { {
                    pickMediaLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                } }
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = ChatAppDimen.padding_32)),
                text = groupName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.group_size, participantSize),
                textAlign = TextAlign.Center
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16))) {
            if (isMediaMessagesLoading) {
                ChatAppPlaceableProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.prog_indic_height))
                )
            } else {
                if (groupMediaMessages.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = dimensionResource(id = ChatAppDimen.padding_16)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Media")
                        ChatAppIconButton(
                            onClick = remember { { onEvent(ChatBoxUiEvent.OnGroupMediaClick) } },
                            imageVector = ChatAppIcons.Default.iosArrowForward
                        )
                    }
                }
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = dimensionResource(id = ChatAppDimen.padding_16)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_8))
            ) {
                items(groupMediaMessages, key = { it.id }) {
                    MediaItem(
                        modifier = Modifier.size(dimensionResource(id = R.dimen.media_item_size)),
                        onClick = remember { { /*TODO*/ } },
                        messageContent = it.messageContent,
                        messageType = it.messageType
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16))) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp / 2),
                    contentPadding = PaddingValues(vertical = dimensionResource(id = ChatAppDimen.padding_16)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16))
                ) {
                    item {
                        AddParticipantItem(onEvent = onEvent)
                    }
                    items(participants, key = { it.participantEmail }) {
                        ChatItem(
                            title = it.participantUsername,
                            imageUrl = it.participantProfilePicUrl
                        )
                    }
                    item {
                        LeaveGroupItem(onEvent = onEvent)
                    }
                }
            }
        }
    }
}

@Composable
private fun AddParticipantScreen(
    modifier: Modifier = Modifier,
    onEvent: (ChatBoxUiEvent) -> Unit,
    participantVal: String,
    isLoading: Boolean
) {
    if (isLoading) {
        ChatAppProgressIndicator(modifier)
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = ChatAppDimen.padding_16)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            ChatAppOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = participantVal,
                onValueChange = remember { { onEvent(ChatBoxUiEvent.OnParticipantValChange(it)) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                labelText = stringResource(id = R.string.participant_email)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_16)))
            ChatAppButton(
                onClick = remember { { onEvent(ChatBoxUiEvent.OnAddParticipantClick) } },
                enabled = participantVal.isNotBlank(),
                text = stringResource(id = R.string.add_participant)
            )
        }
    }
}

private val attachList: List<AttachItem> = listOf(
    AttachItem.Gallery,
    AttachItem.Document
)

private sealed class AttachItem(
    @StringRes val titleId: Int,
    val icon: ImageVector,
    val attachType: AttachType
) {
    data object Gallery : AttachItem(
        titleId = R.string.gallery,
        icon = ChatAppIcons.Filled.gallery,
        attachType = AttachType.GALLERY
    )

    data object Document : AttachItem(
        titleId = R.string.document,
        icon = ChatAppIcons.Filled.document,
        attachType = AttachType.DOCUMENT
    )
}

enum class AttachType {
    GALLERY,
    DOCUMENT
}