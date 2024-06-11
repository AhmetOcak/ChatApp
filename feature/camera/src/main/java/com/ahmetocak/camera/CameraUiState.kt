package com.ahmetocak.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture

data class CameraUiState(
    val screenState: ScreenState = ScreenState.CAMERA,
    val capturedImageUri: Uri? = null,
    val navigateBack: Boolean = false
)

sealed class CameraUiEvent {
    data class OnCaptureImageClick(
        val imageCapture: ImageCapture,
        val context: Context
    ) : CameraUiEvent()
    data class OnSendImageClick(val imageUri: Uri): CameraUiEvent()
}

enum class ScreenState {
    CAMERA,
    CAPTURED_IMAGE
}