package com.ahmetocak.chats

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.chats.components.ChatScreenFloatActionButton
import com.ahmetocak.chats.components.ChatsScreenTopBar
import com.ahmetocak.chats.components.CreateContentItem
import com.ahmetocak.designsystem.components.ChatAppButton
import com.ahmetocak.designsystem.components.ChatAppFilledTextField
import com.ahmetocak.designsystem.components.ChatAppGreyProgressIndicator
import com.ahmetocak.designsystem.components.ChatAppOutlinedTextField
import com.ahmetocak.designsystem.components.ChatAppProgressIndicator
import com.ahmetocak.designsystem.components.ChatAppScaffold
import com.ahmetocak.designsystem.components.FilledChatAppIconButton
import com.ahmetocak.designsystem.components.NetworkImage
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.theme.ChatAppTheme
import com.ahmetocak.model.ChatGroup
import com.ahmetocak.model.ChatGroupParticipants
import com.ahmetocak.model.LoadingState
import com.ahmetocak.ui.BlankUserImage
import com.ahmetocak.ui.ChatItem
import com.ahmetocak.ui.EditableImage
import com.ahmetocak.designsystem.R.dimen as ChatAppDimen

@Composable
internal fun ChatsRoute(
    onNavigateToChatBox: (ChatGroup) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateSettings: () -> Unit,
    viewModel: ChatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent by rememberUpdatedState(
        newValue = { event: ChatsUiEvent -> viewModel.onEvent(event) }
    )

    val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = navigationState) {
        fun performNavigation(onAction: () -> Unit) {
            onAction()
            viewModel.resetNavigation()
        }

        when (val state = navigationState) {
            is NavigationState.ChatBox -> performNavigation {
                onNavigateToChatBox(state.chatGroup)
            }

            is NavigationState.Settings -> performNavigation { onNavigateSettings() }
            is NavigationState.None -> Unit
        }
    }

    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    )
    { uri ->
        if (uri != null) {
            onEvent(ChatsUiEvent.OnGroupImagePicked(uri))
        }
    }

    ChatAppScaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ChatsScreenTopBar(
                screenState = uiState.screenState,
                onEvent = remember { onEvent },
                searchValue = uiState.searchValue
            )
        },
        floatingActionButton = {
            ChatScreenFloatActionButton(
                screenState = uiState.screenState,
                onEvent = remember { onEvent }
            )
        }
    ) { paddingValues ->
        when (uiState.screenState) {
            ScreenState.Chats -> {
                ChatsScreen(
                    modifier = Modifier.padding(paddingValues),
                    chatList = uiState.chatList,
                    onEvent = remember { onEvent },
                    isLoading = uiState.loadingState == LoadingState.Loading
                )
            }

            ScreenState.CreateContact -> {
                CreateChatGroupOrAddContactSection(
                    modifier = Modifier.padding(paddingValues),
                    onEvent = remember { onEvent }
                )
            }

            ScreenState.AddFriend -> {
                AddFriendSection(
                    modifier = Modifier.padding(paddingValues),
                    onEvent = remember { onEvent },
                    newPersonValue = uiState.textFieldValue,
                    isLoading = uiState.loadingState == LoadingState.Loading
                )
            }

            ScreenState.SelectParticipantsForGroup -> {
                SelectParticipantsForGroupSection(
                    modifier = Modifier.padding(paddingValues),
                    onEvent = remember { onEvent },
                    isLoading = uiState.loadingState == LoadingState.Loading,
                    selectedParticipants = uiState.selectedParticipants,
                    addableParticipants = if (uiState.searchValue.isNotBlank()) {
                        uiState.participantList.filter { it.participantUsername.contains(uiState.searchValue) }
                    } else uiState.participantList
                )
            }

            ScreenState.CreateChatGroup -> {
                CreateGroupSection(
                    modifier = Modifier.padding(paddingValues),
                    onEvent = remember { onEvent },
                    groupNameValue = uiState.textFieldValue,
                    isLoading = uiState.loadingState == LoadingState.Loading,
                    onPickImageClick = remember {
                        {
                            pickMediaLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    },
                    addedParticipants = uiState.selectedParticipants,
                    groupImgUri = uiState.selectedGroupImgUri
                )
            }
        }
    }
}


@Composable
private fun ChatsScreen(
    modifier: Modifier = Modifier,
    chatList: List<ChatGroup>,
    onEvent: (ChatsUiEvent) -> Unit,
    isLoading: Boolean
) {
    if (isLoading) {
        ChatAppProgressIndicator(modifier = modifier)
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(chatList, key = { it.id }) { chat ->
                ChatItem(
                    id = chat.id,
                    title = chat.name,
                    imageUrl = chat.imageUrl,
                    onClick = remember {
                        {
                            val participants = chat.participants.filter { it.groupId == chat.id }
                            onEvent(ChatsUiEvent.OnChatItemClick(chat.copy(participants = participants)))
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateChatGroupOrAddContactSection(
    modifier: Modifier = Modifier,
    onEvent: (ChatsUiEvent) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        CreateContentItem(
            title = stringResource(id = R.string.add_friend),
            onClick = remember { { onEvent(ChatsUiEvent.OnAddFriendClick) } },
            icon = ChatAppIcons.Filled.addPerson
        )
        CreateContentItem(
            title = stringResource(id = R.string.create_chat_group),
            onClick = remember { { onEvent(ChatsUiEvent.OnSelectParticipantsForGroup) } },
            icon = ChatAppIcons.Filled.addPersons
        )
    }
}

@Composable
private fun AddFriendSection(
    modifier: Modifier = Modifier,
    onEvent: (ChatsUiEvent) -> Unit,
    newPersonValue: String,
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
                value = newPersonValue,
                onValueChange = remember { { onEvent(ChatsUiEvent.OnValueChanged(it)) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                labelText = stringResource(id = R.string.person_email)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = ChatAppDimen.padding_16)))
            ChatAppButton(
                onClick = remember { { onEvent(ChatsUiEvent.OnSubmitContactClick) } },
                enabled = newPersonValue.isNotBlank(),
                text = stringResource(id = R.string.add_person)
            )
        }
    }
}

@Composable
private fun SelectParticipantsForGroupSection(
    modifier: Modifier = Modifier,
    onEvent: (ChatsUiEvent) -> Unit,
    isLoading: Boolean,
    addableParticipants: List<ChatGroupParticipants>,
    selectedParticipants: List<ChatGroupParticipants>
) {
    if (isLoading) {
        ChatAppProgressIndicator(modifier = modifier)
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16)),
                contentPadding = PaddingValues(horizontal = dimensionResource(id = ChatAppDimen.padding_16))
            ) {
                items(selectedParticipants, key = { it.participantEmail }) { participant ->
                    AddedParticipant(
                        imageUrl = participant.participantProfilePicUrl,
                        username = participant.participantUsername,
                        isRemovable = true,
                        onRemoveClick = remember {
                            { onEvent(ChatsUiEvent.OnRemoveAddedParticipantClick(participant)) }
                        }
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = dimensionResource(id = ChatAppDimen.padding_16))
                    .padding(horizontal = dimensionResource(id = ChatAppDimen.padding_16))
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = dimensionResource(id = ChatAppDimen.padding_16))
            ) {
                items(addableParticipants, key = { it.participantEmail }) { participant ->
                    ChatItem(
                        title = participant.participantUsername,
                        imageUrl = participant.participantProfilePicUrl,
                        onClick = remember {
                            { onEvent(ChatsUiEvent.OnAddParticipantClick(participant)) }
                        },
                        enabled = !selectedParticipants.contains(participant)
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateGroupSection(
    modifier: Modifier = Modifier,
    onEvent: (ChatsUiEvent) -> Unit,
    groupNameValue: String,
    isLoading: Boolean,
    onPickImageClick: () -> Unit,
    addedParticipants: List<ChatGroupParticipants>,
    groupImgUri: Uri?
) {
    if (isLoading) {
        ChatAppGreyProgressIndicator(modifier)
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = ChatAppDimen.padding_16)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_32))
        ) {
            EditableImage(
                imageUrl = groupImgUri.toString(),
                onPickImageClick = onPickImageClick
            )
            ChatAppFilledTextField(
                modifier = Modifier.fillMaxWidth(),
                value = groupNameValue,
                onValueChange = remember { { onEvent(ChatsUiEvent.OnValueChanged(it)) } },
                placeholderText = stringResource(id = R.string.enter_a_group_name)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_16)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_32))
            ) {
                items(addedParticipants, key = { it.participantEmail }) {
                    AddedParticipant(
                        username = it.participantUsername,
                        imageUrl = it.participantProfilePicUrl
                    )
                }
            }
        }
    }
}

@Composable
private fun AddedParticipant(
    imageUrl: String?,
    username: String,
    isRemovable: Boolean = false,
    onRemoveClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimen.padding_8))
    ) {
        Box(
            modifier = Modifier.size(dimensionResource(id = R.dimen.added_participant_box_size)),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (imageUrl != null) {
                NetworkImage(imageUrl = imageUrl, modifier = Modifier.fillMaxSize())
            } else BlankUserImage(modifier = Modifier.fillMaxSize())
            if (isRemovable) {
                FilledChatAppIconButton(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.remove_participant_btn_size)),
                    onClick = onRemoveClick,
                    imageVector = ChatAppIcons.Filled.cancel
                )
            }
        }
        Text(text = username, style = MaterialTheme.typography.labelLarge)
    }
}

@Preview
@Composable
fun ChatsScreenPreview() {
    ChatAppTheme {
        Surface {
            ChatsScreen(
                chatList = emptyList(),
                onEvent = {},
                isLoading = false
            )
        }
    }
}