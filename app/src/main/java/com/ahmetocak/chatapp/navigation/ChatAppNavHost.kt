package com.ahmetocak.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.ahmetocak.camera.navigation.cameraScreen
import com.ahmetocak.camera.navigation.navigateToCamera
import com.ahmetocak.chat_box.navigation.chatBoxScreen
import com.ahmetocak.chat_box.navigation.navigateToChatBox
import com.ahmetocak.chats.navigation.navigateToChats
import com.ahmetocak.login.navigation.loginScreen
import com.ahmetocak.login.navigation.navigateToLogin
import com.ahmetocak.profile.navigation.profileScreen
import com.ahmetocak.signup.navigation.navigateToSignUp
import com.ahmetocak.signup.navigation.signUpScreen
import com.ahmetocak.user_detail.navigation.navigateToUserDetail
import com.ahmetocak.user_detail.navigation.userDetailScreen

@Composable
fun ChatAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        topLevelDestinationsGraph(
            onNavigateToChatBox = navController::navigateToChatBox,
            onNavigateToUserDetail = navController::navigateToUserDetail,
            onNavigateCallInfo = {}
        )
        chatBoxScreen(
            upPress = navController::navigateUp,
            navigateChatDetail = {},
            navigateCamera = navController::navigateToCamera,
            navigateChatDocs = {}
        )
        loginScreen(
            navigateToChats = {
                navController.navigateToChats(navOptions = navOptions { popUpTo(0) })
            },
            navigateToSignUp = navController::navigateToSignUp,
        )
        signUpScreen(
            navigateUp = navController::navigateUp,
            navigateToChats = navController::navigateToChats,
            navigateToLogin = {
                navController.navigateToLogin(navOptions = navOptions { popUpTo(0) })
            }
        )
        userDetailScreen(upPress = navController::navigateUp)
        profileScreen(
            upPress = navController::navigateUp,
            onNavigateLogin = navController::navigateToLogin
        )
        cameraScreen(upPress = navController::navigateUp)
    }
}