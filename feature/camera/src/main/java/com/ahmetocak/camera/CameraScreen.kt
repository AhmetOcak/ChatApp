package com.ahmetocak.camera

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmetocak.designsystem.components.FilledChatAppIconButton
import com.ahmetocak.designsystem.components.NetworkImage
import com.ahmetocak.designsystem.icons.ChatAppIcons
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun CameraRoute(
    modifier: Modifier = Modifier,
    upPress: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraPermissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            listOf(Manifest.permission.CAMERA)
        } else listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    if (uiState.navigateBack) upPress()

    if (cameraPermissionState.allPermissionsGranted) {
        when (uiState.screenState) {
            ScreenState.CAMERA -> {
                CameraScreen(
                    modifier = modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    onEvent = viewModel::onEvent,
                    context = LocalContext.current,
                    lifecycleOwner = LocalLifecycleOwner.current
                )
            }

            ScreenState.CAPTURED_IMAGE -> {
                CapturedImageScreen(
                    modifier = modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    imgUri = uiState.capturedImageUri,
                    onEvent = viewModel::onEvent,
                    isMessageSending = uiState.isMessageSending
                )
            }
        }
    } else {
        SideEffect {
            cameraPermissionState.launchMultiplePermissionRequest()
        }
    }
}

@Composable
private fun CameraScreen(
    modifier: Modifier,
    onEvent: (CameraUiEvent) -> Unit,
    context: Context,
    lifecycleOwner: LifecycleOwner
) {
    val cameraProviderFuture = remember(context) { ProcessCameraProvider.getInstance(context) }
    val cameraProvider = remember(cameraProviderFuture) { cameraProviderFuture.get() }
    val executor = remember(context) { ContextCompat.getMainExecutor(context) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var cameraSelector: CameraSelector? by remember { mutableStateOf(null) }
    var camera: Camera? by remember { mutableStateOf(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }

    val lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }

    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()
                    imageCapture = ImageCapture.Builder()
                        .setTargetRotation(previewView.display.rotation)
                        .build()

                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector as CameraSelector,
                        imageCapture,
                        preview
                    )
                }, executor)

                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 64.dp)
                .border(4.dp, Color.White, CircleShape)
                .aspectRatio(1f),
            onClick = remember {
                { onEvent(CameraUiEvent.OnCaptureImageClick(imageCapture!!, context)) }
            }
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                imageVector = ChatAppIcons.Filled.capture,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
private fun CapturedImageScreen(
    modifier: Modifier,
    imgUri: Uri?,
    onEvent: (CameraUiEvent) -> Unit,
    isMessageSending: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NetworkImage(modifier = Modifier.fillMaxSize(), imageUrl = imgUri.toString())
        }
        AnimatedVisibility(visible = isMessageSending, enter = scaleIn(), exit = scaleOut()) {
            CircularProgressIndicator(modifier = Modifier.padding(bottom = 32.dp, end = 16.dp))
        }
        AnimatedVisibility(visible = !isMessageSending, enter = scaleIn(), exit = scaleOut()) {
            FilledChatAppIconButton(
                onClick = remember {
                    { if (imgUri != null) onEvent(CameraUiEvent.OnSendImageClick(imgUri)) }
                },
                imageVector = ChatAppIcons.Default.send,
                modifier = Modifier
                    .padding(bottom = 32.dp, end = 16.dp)
                    .size(48.dp)
            )
        }
    }
}