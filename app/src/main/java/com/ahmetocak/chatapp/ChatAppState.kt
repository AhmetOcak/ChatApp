package com.ahmetocak.chatapp

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.ahmetocak.chats.navigation.CHATS_ROUTE
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.domain.usecase.auth.IsAnyUserLoggedInUseCase
import com.ahmetocak.login.navigation.LOGIN_ROUTE
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

    val startDestination: String =
        if (IsAnyUserLoggedInUseCase.invoke()) CHATS_ROUTE else LOGIN_ROUTE
}