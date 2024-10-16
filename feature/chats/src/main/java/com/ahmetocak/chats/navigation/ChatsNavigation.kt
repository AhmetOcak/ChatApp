package com.ahmetocak.chats.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.chats.ChatsRoute
import com.ahmetocak.model.ChatGroup

const val CHATS_ROUTE = "chats_route"

fun NavHostController.navigateToChats(navOptions: NavOptions? = null) =
    navigate(CHATS_ROUTE, navOptions)

fun NavGraphBuilder.chatsScreen(
    onNavigateToChatBox: (ChatGroup) -> Unit,
    onNavigateSettings: () -> Unit
) {
    composable(CHATS_ROUTE) {
        ChatsRoute(
            onNavigateToChatBox = onNavigateToChatBox,
            onNavigateSettings = onNavigateSettings
        )
    }
}