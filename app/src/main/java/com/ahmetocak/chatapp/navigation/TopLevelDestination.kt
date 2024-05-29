package com.ahmetocak.chatapp.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.ahmetocak.calls.navigation.CALLS_ROUTE
import com.ahmetocak.chatapp.R
import com.ahmetocak.chats.navigation.CHATS_ROUTE
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.ahmetocak.settings.navigation.SETTINGS_ROUTE
import com.ahmetocak.stories.navigation.STORIES_ROUTE

enum class TopLevelDestination(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val route: String
) {
    CHATS(
        R.string.chats,
        ChatAppIcons.Filled.chat,
        ChatAppIcons.Outlined.chat,
        CHATS_ROUTE
    ),
    STORIES(
        R.string.stories,
        ChatAppIcons.Filled.stories,
        ChatAppIcons.Outlined.stories,
        STORIES_ROUTE
    ),
    CALLS(
        R.string.calls,
        ChatAppIcons.Filled.call,
        ChatAppIcons.Outlined.call,
        CALLS_ROUTE
    ),
    SETTINGS(
        R.string.settings,
        ChatAppIcons.Filled.settings,
        ChatAppIcons.Outlined.settings,
        SETTINGS_ROUTE
    )
}