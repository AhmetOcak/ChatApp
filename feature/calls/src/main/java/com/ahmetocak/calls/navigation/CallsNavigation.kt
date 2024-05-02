package com.ahmetocak.calls.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.calls.CallsRoute

const val CALLS_ROUTE = "calls_route"

fun NavController.navigateToCalls(navOptions: NavOptions) = navigate(CALLS_ROUTE, navOptions)

fun NavGraphBuilder.callsScreen(onNavigateCallInfo: (String) -> Unit) {
    composable(CALLS_ROUTE) {
        CallsRoute(onNavigateCallInfo = onNavigateCallInfo)
    }
}