package com.ahmetocak.user_detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.user_detail.UserDetailRoute

const val USER_DETAIL_ROUTE = "user_detail_route"

fun NavController.navigateUserDetail(navOptions: NavOptions) = navigate(USER_DETAIL_ROUTE)

fun NavGraphBuilder.userDetailScreen(upPress: () -> Unit) {
    composable(USER_DETAIL_ROUTE) {
        UserDetailRoute(upPress = upPress)
    }
}