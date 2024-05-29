package com.ahmetocak.chatapp

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ahmetocak.chatapp.navigation.ChatAppNavHost
import com.ahmetocak.chatapp.navigation.TopLevelDestination
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.designsystem.theme.ChatAppTheme
import kotlinx.coroutines.CoroutineScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatApp(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    appState: ChatAppState = rememberChatAppState()
) {
    ChatAppTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        Surface {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarHostState,
                        modifier = Modifier.padding(4.dp),
                        snackbar = { snackbarData -> Snackbar(snackbarData) }
                    )
                },
                bottomBar = {
                    ChatAppNavigationBar(
                        topLevelDestinations = TopLevelDestination.entries,
                        currentTopLevelDestination = appState.currentTopLevelDestination,
                        onNavigateToDestination = appState::navigateToTopLevelDestination
                    )
                }
            ) {
                ChatAppNavHost(
                    navController = appState.navController,
                    startDestination = appState.startDestination
                )
            }
        }
    }
}

@Composable
private fun ChatAppNavigationBar(
    topLevelDestinations: List<TopLevelDestination>,
    currentTopLevelDestination: TopLevelDestination?,
    onNavigateToDestination: (TopLevelDestination) -> Unit
) {
    if (currentTopLevelDestination == null) return
    NavigationBar {
        topLevelDestinations.forEach { destination ->
            NavigationBarItem(
                selected = currentTopLevelDestination.route == destination.route,
                onClick = {
                    onNavigateToDestination(destination)
                },
                icon = {
                    Icon(
                        imageVector = if (currentTopLevelDestination.route == destination.route)
                            destination.selectedIcon else destination.unSelectedIcon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(text = stringResource(id = destination.title))
                }
            )
        }
    }
}

@Composable
private fun rememberChatAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: SnackbarManager = SnackbarManager,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(navController, snackbarHostState, coroutineScope) {
    ChatAppState(
        navController,
        snackbarHostState,
        snackbarManager,
        context,
        coroutineScope
    )
}