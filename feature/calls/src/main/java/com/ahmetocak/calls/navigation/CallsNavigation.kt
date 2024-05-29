package com.ahmetocak.calls.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.calls.CallsRoute

const val CALLS_ROUTE = "calls_route"

fun NavHostController.navigateToCalls(navOptions: NavOptions? = null) = navigate(CALLS_ROUTE, navOptions)

fun NavGraphBuilder.callsScreen(onNavigateCallInfo: (String) -> Unit) {
    composable(CALLS_ROUTE) {
        CallsRoute(onNavigateCallInfo = onNavigateCallInfo)
    }
}