package com.ahmetocak.chat_box.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmetocak.chat_box.ChatBoxRoute
import com.ahmetocak.common.ext.encodeForSaveNav
import com.ahmetocak.model.ChatGroup
import kotlinx.serialization.json.Json

const val CHAT_BOX_ROUTE = "chat_box_route"
const val CHAT_GROUP = "chat_group"

fun NavHostController.navigateToChatBox(
    chatGroup: ChatGroup,
    navOptions: NavOptions? = null
) = navigate(
    route = "$CHAT_BOX_ROUTE/${Json.encodeToString(ChatGroup.serializer(), chatGroup).encodeForSaveNav()}",
    navOptions = navOptions
)

fun NavGraphBuilder.chatBoxScreen(
    upPress: () -> Unit,
    navigateChatDetail: (String) -> Unit,
    navigateChatDocs: (String) -> Unit,
    navigateCamera: (Int, String, String, String?) -> Unit,
) {
    composable(
        route = "$CHAT_BOX_ROUTE/{$CHAT_GROUP}",
        arguments = listOf(
            navArgument(CHAT_GROUP) { type = NavType.StringType }
        )
    ) {
        ChatBoxRoute(
            upPress = upPress,
            navigateCamera = navigateCamera,
            navigateChatDetail = navigateChatDetail,
            navigateChatDocs = navigateChatDocs
        )
    }
}