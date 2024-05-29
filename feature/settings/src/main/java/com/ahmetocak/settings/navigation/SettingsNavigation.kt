package com.ahmetocak.settings.navigation

import androidx.compose.foundation.layout.Column
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val SETTINGS_ROUTE = "settings_route"

fun NavHostController.navigateToSettings(navOptions: NavOptions? = null) = navigate(SETTINGS_ROUTE, navOptions)

fun NavGraphBuilder.settingsScreen() {
    composable(SETTINGS_ROUTE) {
        Column {
            // Screen
        }
    }
}