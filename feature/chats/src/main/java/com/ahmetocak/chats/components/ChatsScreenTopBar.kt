package com.ahmetocak.chats.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.ahmetocak.chats.ChatsUiEvent
import com.ahmetocak.chats.R
import com.ahmetocak.chats.ScreenState
import com.ahmetocak.designsystem.components.ChatAppIconButton
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.designsystem.R.string as ChatAppStrings
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun ChatsScreenTopBar(
    screenState: ScreenState,
    onEvent: (ChatsUiEvent) -> Unit,
    searchValue: String
) {
    var showSearchField by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            when (screenState) {
                ScreenState.Chats -> Text(text = stringResource(id = ChatAppStrings.app_name))
                ScreenState.CreateContact -> Text(text = stringResource(id = R.string.create_contact))
                ScreenState.AddFriend -> Text(text = stringResource(id = R.string.add_friend))
                ScreenState.SelectParticipantsForGroup -> {
                    Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = ChatAppDimens.padding_2))) {
                        Text(text = stringResource(id = R.string.create_group))
                        Text(
                            text = stringResource(id = R.string.add_participant),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                ScreenState.CreateChatGroup -> Text(text = stringResource(id = R.string.create_chat_group))
            }
        },
        actions = {
            AnimatedVisibility(
                visible = screenState is ScreenState.Chats,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ChatAppIconButton(
                    onClick = remember { { onEvent(ChatsUiEvent.OnSettingsClick) } },
                    imageVector = ChatAppIcons.Filled.settings
                )
            }
            AnimatedVisibility(
                visible = screenState is ScreenState.SelectParticipantsForGroup && !showSearchField,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ChatAppIconButton(
                    onClick = remember { { showSearchField = true } },
                    imageVector = ChatAppIcons.Filled.search
                )
            }
            AnimatedVisibility(
                visible = showSearchField,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally()
            ) {
                SearchField(
                    searchValue = searchValue,
                    onSearchValueChange = remember { { onEvent(ChatsUiEvent.OnSearchValueChanged(it)) } },
                    onBackClick = remember {
                        {
                            showSearchField = false
                            onEvent(ChatsUiEvent.OnSearchValueChanged(""))
                        }
                    }
                )
            }
        },
        navigationIcon = {
            when (screenState) {
                ScreenState.Chats -> {}
                else -> {
                    if (!showSearchField) {
                        ChatAppIconButton(
                            onClick = remember { { onEvent(ChatsUiEvent.OnBackClick) } },
                            imageVector = ChatAppIcons.Default.arrowBack
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun SearchField(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = ChatAppDimens.padding_16)),
        value = searchValue,
        onValueChange = onSearchValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        decorationBox = { innerTextField ->
            Surface(
                modifier = Modifier.height(dimensionResource(id = R.dimen.search_user_field_height)),
                shape = RoundedCornerShape(50),
                color = TextFieldDefaults.colors().focusedContainerColor
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChatAppIconButton(
                        onClick = onBackClick,
                        imageVector = ChatAppIcons.Default.arrowBack
                    )
                    Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        if (searchValue.isBlank()) {
                            Text(
                                text = stringResource(id = R.string.search_user),
                                style = MaterialTheme.typography.labelLarge,
                                color = TextFieldDefaults.colors().unfocusedPlaceholderColor
                            )
                        }
                        innerTextField()
                    }
                }
            }
        },
        textStyle = TextStyle(color = TextFieldDefaults.colors().focusedTextColor)
    )
}