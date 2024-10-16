package com.ahmetocak.database.utils

import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText

/**
 * A generic function for making database-related calls with error handling.
 *
 * This function is designed to be used with suspending database operations. It wraps the database
 * operation with a try-catch block to handle exceptions and provides a uniform way of dealing with responses.
 *
 * @param call A suspending lambda function representing the database operation.
 * @return A sealed class [Response] containing either the success data or an error message.
 */
suspend inline fun <T> dbCall(crossinline call: suspend () -> T): Response<T> {
    return try {
        Response.Success(data = call())
    } catch (e: Exception) {
        Response.Error(errorMessage = e.message?.let { message ->
            UiText.DynamicString(message)
        } ?: UiText.DynamicString(e.stackTraceToString()))
    }
}