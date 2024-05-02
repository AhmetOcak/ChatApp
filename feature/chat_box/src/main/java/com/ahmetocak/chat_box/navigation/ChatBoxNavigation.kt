package com.ahmetocak.chat_box.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.chat_box.ChatBoxRoute

const val CHAT_BOX_ROUTE = "chat_box_route"
const val CHAT_BOX_ID = "id"

fun NavController.navigateToChatBox(navOptions: NavOptions) =
    navigate("$CHAT_BOX_ROUTE/$CHAT_BOX_ID")

fun NavGraphBuilder.chatBoxScreen() {
    composable("$CHAT_BOX_ROUTE/{$CHAT_BOX_ID}") {
        ChatBoxRoute()
    }
}