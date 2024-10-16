package com.ahmetocak.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.settings.SettingsRoute

const val SETTINGS_ROUTE = "settings_route"

fun NavHostController.navigateToSettings(navOptions: NavOptions? = null) =
    navigate(SETTINGS_ROUTE, navOptions)

fun NavGraphBuilder.settingsScreen(
    upPress: () -> Unit,
    onNavigateLogin: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    composable(SETTINGS_ROUTE) {
        SettingsRoute(
            upPress = upPress,
            onNavigateLogin = onNavigateLogin,
            onNavigateToProfile = onNavigateToProfile
        )
    }
}