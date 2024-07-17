package com.ahmetocak.chat_box.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmetocak.chat_box.ChatBoxRoute
import com.ahmetocak.model.ChatGroup

const val CHAT_BOX_ROUTE = "chat_box_route"
const val FRIEND_EMAIL = "friend_email"
const val FRIEND_USERNAME = "friend_username"
const val FRIEND_PROF_PIC_URL = "friend_prof_pic_url"
const val FRIENDSHIP_ID = "friendship_id"

fun NavHostController.navigateToChatBox(
    chatGroup: ChatGroup,
    navOptions: NavOptions? = null
) = navigate(
    route = "$CHAT_BOX_ROUTE/$chatGroup",
    navOptions = navOptions
)

fun NavGraphBuilder.chatBoxScreen(
    upPress: () -> Unit,
    navigateChatDetail: (String) -> Unit,
    navigateChatDocs: (String) -> Unit,
    navigateCamera: (Int, String, String, String?, String) -> Unit,
) {
    composable(
        route = "$CHAT_BOX_ROUTE/{$FRIENDSHIP_ID}/{$FRIEND_EMAIL}/{$FRIEND_USERNAME}/{${FRIEND_PROF_PIC_URL}}",
        arguments = listOf(
            navArgument(FRIENDSHIP_ID) { type = NavType.IntType },
            navArgument(FRIEND_EMAIL) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(FRIEND_USERNAME) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(FRIEND_PROF_PIC_URL) {
                type = NavType.StringType
                nullable = true
            }
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