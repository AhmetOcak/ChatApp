package com.ahmetocak.ui.chat_bubble.doc_manager

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

internal suspend fun downloadDocumentToPermanentStorage(
    context: Context,
    pdfUrl: String,
    fileName: String
): Uri? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(pdfUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                return@withContext null
            }

            val inputStream: InputStream = connection.inputStream
            val file = File(context.cacheDir, "$fileName.pdf")
            val outputStream = FileOutputStream(file)

            inputStream.apply {
                copyTo(outputStream)
                close()
            }
            outputStream.close()

            FileProvider.getUriForFile(context, "com.ahmetocak.chatapp.provider", file)
        } catch (e: Exception) {
            Log.e("downloadPdfToPermanentStorage", e.stackTraceToString())
            null
        }
    }
}