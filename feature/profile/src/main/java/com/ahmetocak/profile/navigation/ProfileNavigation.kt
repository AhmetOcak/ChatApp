package com.ahmetocak.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.profile.ProfileRoute

const val PROFILE_ROUTE = "profile_route"

fun NavController.navigateProfileScreen(navOptions: NavOptions) = navigate(PROFILE_ROUTE)

fun NavGraphBuilder.profileScreen(upPress: () -> Unit) {
    composable(PROFILE_ROUTE) {
        ProfileRoute(upPress = upPress)
    }
}