package com.ahmetocak.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.profile.ProfileRoute

const val PROFILE_ROUTE = "profile_route"

fun NavHostController.navigateToProfileScreen(navOptions: NavOptions? = null) =
    navigate(PROFILE_ROUTE, navOptions)

fun NavGraphBuilder.profileScreen(upPress: () -> Unit) {
    composable(PROFILE_ROUTE) {
        ProfileRoute(upPress = upPress)
    }
}