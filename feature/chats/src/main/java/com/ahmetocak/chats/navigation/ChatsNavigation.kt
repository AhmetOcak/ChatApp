package com.ahmetocak.chats.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.chats.ChatsRoute

const val CHATS_ROUTE = "chats_route"

fun NavHostController.navigateToChats(navOptions: NavOptions? = null) = navigate(CHATS_ROUTE, navOptions)

fun NavGraphBuilder.chatsScreen(onNavigateToChatBox: (Int) -> Unit) {
    composable(CHATS_ROUTE) {
        ChatsRoute(onNavigateToChatBox = onNavigateToChatBox)
    }
}