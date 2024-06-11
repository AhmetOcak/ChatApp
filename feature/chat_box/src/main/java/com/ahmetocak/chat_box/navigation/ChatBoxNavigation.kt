package com.ahmetocak.chat_box.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmetocak.chat_box.ChatBoxRoute

const val CHAT_BOX_ROUTE = "chat_box_route"
const val CHAT_BOX_ID = "id"
const val FRIEND_EMAIL = "friend_email"
const val FRIEND_USERNAME = "friend_username"
const val FRIEND_PROF_PIC_URL = "friend_prof_pic_url"

fun NavHostController.navigateToChatBox(
    chatBoxId: Int,
    friendEmail: String,
    friendUsername: String,
    friendProfPicUrl: String?,
    navOptions: NavOptions? = null
) = navigate(
    route = "$CHAT_BOX_ROUTE/$chatBoxId/$friendEmail/$friendUsername/$friendProfPicUrl",
    navOptions = navOptions
)

fun NavGraphBuilder.chatBoxScreen(
    upPress: () -> Unit,
    navigateChatDetail: (String) -> Unit,
    navigateChatDocs: (String) -> Unit,
    navigateCamera: (String, String, String?, String, ) -> Unit,
) {
    composable(
        route = "$CHAT_BOX_ROUTE/{$CHAT_BOX_ID}/{$FRIEND_EMAIL}/{$FRIEND_USERNAME}/{${FRIEND_PROF_PIC_URL}}",
        arguments = listOf(
            navArgument(CHAT_BOX_ID) { NavType.IntType },
            navArgument(FRIEND_EMAIL) { NavType.StringType },
            navArgument(FRIEND_USERNAME) { NavType.StringType },
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