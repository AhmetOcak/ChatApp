package com.ahmetocak.chat_box

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.ext.failureMessage

fun viewPdf(context: Context, uri: Uri) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, context.contentResolver.getType(uri))
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    } catch (e: Exception) {
        SnackbarManager.showMessage(e.failureMessage())
        Log.e("viewPdf", e.stackTraceToString())
    }
}

@OptIn(ExperimentalCoilApi::class)
fun viewImage(context: Context, imageUrl: String) {
    try {
        context.imageLoader.diskCache?.openSnapshot(imageUrl)?.use { snapshot ->
            val imageFile = snapshot.data.toFile()
            val imageUri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                imageFile
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(imageUri, "image/*")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
            context.startActivity(intent)
        }
    } catch (e: Exception) {
        SnackbarManager.showMessage(e.failureMessage())
        Log.e("viewImage", e.stackTraceToString())
    }
}