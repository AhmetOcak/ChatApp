package com.ahmetocak.chatapp

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ahmetocak.chatapp.navigation.ChatAppNavHost
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.designsystem.theme.ChatAppTheme
import kotlinx.coroutines.CoroutineScope
import com.ahmetocak.designsystem.R.dimen as ChatAppDimens

@Composable
fun ChatApp(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    appState: ChatAppState = rememberChatAppState()
) {
    ChatAppTheme(darkTheme = darkTheme, dynamicColor = dynamicColor) {
        Surface {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarHostState,
                        modifier = Modifier.padding(dimensionResource(id = ChatAppDimens.padding_4)),
                        snackbar = { snackbarData -> Snackbar(snackbarData) }
                    )
                }
            ) { paddingValues ->
                ChatAppNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    navController = appState.navController,
                    startDestination = appState.startDestination
                )
            }
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