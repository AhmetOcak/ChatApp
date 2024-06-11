package com.ahmetocak.camera.capture_img

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

class ImageManipulation(private val contentResolver: ContentResolver) {

    fun manipulateImage(imgUri: Uri) {
        if (getImagePixels(imgUri) > 1000) {
            reduceImagePixelSize(imgUri)
        }

        var coefficient = 1
        while (calculateImgFileSize(imgUri) > 1024 && coefficient <= 5) {
            reduceImageFileSize(imgUri, coefficient)
            coefficient++
        }
    }

    private fun getImagePixels(imgUri: Uri): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        val path: String? = imgUri.path
        BitmapFactory.decodeFile(path, options)

        return options.outHeight
    }

    private fun reduceImagePixelSize(imgUri: Uri) {
        val widthInPixels = 1000
        val heightInPixels = 1000

        val originalBitmap = BitmapFactory.decodeFile(imgUri.path)
        val scaledBitmap =
            Bitmap.createScaledBitmap(originalBitmap, widthInPixels, heightInPixels, true)

        val file = imgUri.path?.let { File(it) }
        val outputStream = FileOutputStream(file)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    private fun calculateImgFileSize(imgUri: Uri): Float {
        val afd = contentResolver.openAssetFileDescriptor(imgUri, "r")
        val fileSizeKb = afd?.length?.toFloat()?.div(1024)
        afd?.close()
        return fileSizeKb ?: 0f
    }

    private fun reduceImageFileSize(imgUri: Uri, coefficient: Int) {
        val bitmap = BitmapFactory.decodeFile(imgUri.path)
        val file = imgUri.path?.let { File(it) }
        val outputStream = FileOutputStream(file)

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            when (coefficient) {
                1 -> {
                    80
                }
                2 -> {
                    60
                }
                3 -> {
                    40
                }
                4 -> {
                    20
                }
                else -> {
                    0
                }
            },
            outputStream
        )

        outputStream.close()
    }
}