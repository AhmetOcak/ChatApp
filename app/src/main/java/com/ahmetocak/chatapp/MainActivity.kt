package com.ahmetocak.chatapp

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val notificationPermissionState = rememberPermissionState(
                    permission = Manifest.permission.POST_NOTIFICATIONS
                )

                if (!notificationPermissionState.status.isGranted) {
                    SideEffect {
                        notificationPermissionState.launchPermissionRequest()
                    }
                }
            }

            ChatApp(
                darkTheme = uiState.isDarkMode,
                dynamicColor = uiState.isDynamicColor
            )
        }
    }
}