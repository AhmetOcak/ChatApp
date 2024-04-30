package com.ahmetocak.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.login.LoginRoute

const val LOGIN_ROUTE = "login_route"

fun NavController.navigateToLogin(navOptions: NavOptions) = navigate(LOGIN_ROUTE, navOptions)

fun NavGraphBuilder.loginScreen(navigateToChats: () -> Unit, navigateToSignUp: () -> Unit) {
    composable(LOGIN_ROUTE) {
        LoginRoute(navigateToChats = navigateToChats, navigateToSignUp = navigateToSignUp)
    }
}