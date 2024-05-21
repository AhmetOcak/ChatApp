package com.ahmetocak.domain.usecase.utils

import android.util.Log
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi

internal const val USER_TAG = "USER CACHING"

@OptIn(ExperimentalCoilApi::class)
internal fun clearCoilCache(imageLoader: ImageLoader) {
    try {
        imageLoader.apply {
            memoryCache?.clear()
            diskCache?.clear()
        }
    } catch (e: Exception) {
        Log.e(e.message, e.stackTraceToString())
    }
}