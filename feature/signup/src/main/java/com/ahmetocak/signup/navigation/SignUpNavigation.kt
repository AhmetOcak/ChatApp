package com.ahmetocak.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ahmetocak.signup.SignUpRoute

const val SIGN_UP_ROUTE = "sign_up_route"

fun NavController.navigateToSignUp(navOptions: NavOptions) = navigate(SIGN_UP_ROUTE, navOptions)

fun NavGraphBuilder.signUpScreen(
    navigateUp: () -> Unit,
    navigateToChats: () -> Unit,
    navigateToLogin: () -> Unit
) {
    composable(SIGN_UP_ROUTE) {
        SignUpRoute(
            navigateUp = navigateUp,
            navigateToChats = navigateToChats,
            navigateToLogin = navigateToLogin
        )
    }
}