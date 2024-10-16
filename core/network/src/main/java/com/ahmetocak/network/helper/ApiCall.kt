package com.ahmetocak.network.helper

import android.util.Log
import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import java.io.IOException

/**
 * A generic function for making API calls with error handling.
 *
 * This function is designed to be used with suspending API calls. It wraps the API call
 * with try-catch blocks to handle exceptions and provides a uniform way of dealing with responses.
 *
 * @param call A suspending lambda function representing the API call.
 * @return A sealed class [Response] containing either the success data or an error message.
 */
suspend inline fun <T> apiCall(noinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: IOException) {
        Response.Error(errorMessage = UiText.DynamicString(e.stackTraceToString()))
    } catch (e: Exception) {
        Log.e("API_CALL ${call.javaClass.name}", e.stackTraceToString())
        Response.Error(errorMessage = e.message?.let { message ->
            UiText.DynamicString(message)
        } ?: UiText.DynamicString("Something went wrong"))
    }
}