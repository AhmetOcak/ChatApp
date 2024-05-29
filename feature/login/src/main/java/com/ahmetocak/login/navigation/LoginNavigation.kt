package com.ahmetocak.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.login.LoginRoute

const val LOGIN_ROUTE = "login_route"

fun NavHostController.navigateToLogin(navOptions: NavOptions? = null) = navigate(LOGIN_ROUTE, navOptions)

fun NavGraphBuilder.loginScreen(navigateToChats: () -> Unit, navigateToSignUp: () -> Unit) {
    composable(LOGIN_ROUTE) {
        LoginRoute(navigateToChats = navigateToChats, navigateToSignUp = navigateToSignUp)
    }
}