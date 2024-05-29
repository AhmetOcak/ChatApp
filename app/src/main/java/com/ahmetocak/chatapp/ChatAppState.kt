package com.ahmetocak.chatapp

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.ahmetocak.calls.navigation.navigateToCalls
import com.ahmetocak.chatapp.navigation.TopLevelDestination
import com.ahmetocak.chats.navigation.navigateToChats
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.domain.usecase.auth.IsAnyUserLoggedInUseCase
import com.ahmetocak.login.navigation.LOGIN_ROUTE
import com.ahmetocak.settings.navigation.navigateToSettings
import com.ahmetocak.stories.navigation.navigateToStories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class ChatAppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager,
    private val context: Context,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.messages.filterNotNull().collect { message ->
                val text = message.first.asString(context)
                snackbarHostState.showSnackbar(
                    message = text,
                    withDismissAction = true,
                    duration = message.second ?: SnackbarDuration.Short
                )
                snackbarManager.clean()
            }
        }
    }

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val startDestination: String =
        if (IsAnyUserLoggedInUseCase.invoke()) TopLevelDestination.CHATS.route else LOGIN_ROUTE

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            TopLevelDestination.CHATS.route -> TopLevelDestination.CHATS
            TopLevelDestination.CALLS.route -> TopLevelDestination.CALLS
            TopLevelDestination.STORIES.route -> TopLevelDestination.STORIES
            TopLevelDestination.SETTINGS.route -> TopLevelDestination.SETTINGS
            else -> null
        }

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(TopLevelDestination.CHATS.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        when (topLevelDestination) {
            TopLevelDestination.CHATS -> navController.navigateToChats(navOptions = topLevelNavOptions)
            TopLevelDestination.STORIES -> navController.navigateToStories(navOptions = topLevelNavOptions)
            TopLevelDestination.CALLS -> navController.navigateToCalls(navOptions = topLevelNavOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettings(navOptions = topLevelNavOptions)
        }
    }
}