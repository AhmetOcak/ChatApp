package com.ahmetocak.chatapp.navigation

import androidx.navigation.NavGraphBuilder
import com.ahmetocak.calls.navigation.callsScreen
import com.ahmetocak.chats.navigation.chatsScreen
import com.ahmetocak.settings.navigation.settingsScreen
import com.ahmetocak.stories.navigation.storiesScreen

fun NavGraphBuilder.topLevelDestinationsGraph(
    onNavigateToChatBox: (Int) -> Unit,
    onNavigateToUserDetail: (String) -> Unit,
    onNavigateCallInfo: (String) -> Unit
) {
    chatsScreen(onNavigateToChatBox = onNavigateToChatBox)
    storiesScreen(onNavigateToUserDetail = onNavigateToUserDetail)
    callsScreen(onNavigateCallInfo = onNavigateCallInfo)
    settingsScreen()
}