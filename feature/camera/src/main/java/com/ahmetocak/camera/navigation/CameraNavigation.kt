package com.ahmetocak.camera.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmetocak.camera.CameraRoute

const val MESSAGE_BOX_ID = "message_box_id"
const val CAMERA_ROUTE = "camera_route"
const val SENDER_EMAIL = "sender_email"
const val SENDER_IMG_URL = "sender_img_url"
const val SENDER_USERNAME = "sender_username"

fun NavHostController.navigateToCamera(
    messageBoxId: Int,
    senderEmail: String,
    senderUsername: String,
    senderImgUrl: String?,
    navOptions: NavOptions? = null
)
= navigate(
    route = "$CAMERA_ROUTE/$messageBoxId/$senderEmail/$senderImgUrl/$senderUsername",
    navOptions = navOptions
)

fun NavGraphBuilder.cameraScreen(upPress: () -> Unit) {
    composable(
        route = "$CAMERA_ROUTE/{$MESSAGE_BOX_ID}/{$SENDER_EMAIL}/{$SENDER_IMG_URL}/{$SENDER_USERNAME}",
        arguments = listOf(
            navArgument(MESSAGE_BOX_ID) { NavType.IntType },
            navArgument(SENDER_EMAIL) { NavType.StringType },
            navArgument(SENDER_IMG_URL) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(SENDER_USERNAME) { NavType.StringType }
        )
    ) {
        CameraRoute(upPress = upPress)
    }
}