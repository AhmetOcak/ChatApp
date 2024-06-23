package com.ahmetocak.camera.capture_img

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.UiText
import java.text.SimpleDateFormat
import java.util.Locale

internal object CameraController {

    private var capturedPhotoUri: Uri? = null

    fun takePhoto(imageCapture: ImageCapture, context: Context): Uri? {
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    "Pictures/ChatApp"
                )
            } else {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    SnackbarManager.showMessage(
                        UiText.DynamicString(exc.message ?: "Something went wrong!")
                    )
                    Log.e("takePhoto", exc.stackTraceToString())
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    capturedPhotoUri = output.savedUri
                }
            }
        )

        return capturedPhotoUri
    }

    fun resetCapturedPhotoUri() {
        capturedPhotoUri = null
    }
}