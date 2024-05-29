package com.ahmetocak.user_detail.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.user_detail.UserDetailRoute

const val USER_DETAIL_ROUTE = "user_detail_route"
const val USER_EMAIL = "user_email"

fun NavHostController.navigateToUserDetail(userEmail: String, navOptions: NavOptions? = null) =
    navigate("$USER_DETAIL_ROUTE/$userEmail")

fun NavGraphBuilder.userDetailScreen(upPress: () -> Unit) {
    composable("$USER_DETAIL_ROUTE/{$USER_EMAIL}") {
        UserDetailRoute(upPress = upPress)
    }
}