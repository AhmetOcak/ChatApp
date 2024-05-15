package com.ahmetocak.common.ext

import android.util.Log
import android.util.Patterns
import com.ahmetocak.common.UiText
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import com.ahmetocak.designsystem.R.string as AppStrings

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.length >= 6
}

fun String.isValidName(): Boolean {
    return this.isNotBlank()
}

fun String.confirmPassword(password: String): Boolean {
    return this == password
}

fun Exception?.failureMessage(): UiText {
    return this?.message?.let { message ->
        UiText.DynamicString(message)
    } ?: run {
        UiText.StringResource(AppStrings.unknown_error)
    }
}

fun getCurrentDate(): String {
    val currentDate = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
    return currentDate.format(formatter)
}

fun String.showMessageSendTime(): String {
    try {
        val formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")
        val currentD = LocalDateTime.now().format(formatter)
        val messageD = this

        val currentDate = LocalDateTime.parse(currentD, formatter).toLocalDate()
        val messageDate = LocalDateTime.parse(messageD, formatter).toLocalDate()

        val year = Period.between(currentDate, messageDate).years
        val month = Period.between(currentDate, messageDate).months
        val days = Period.between(currentDate, messageDate).days

        return if (year != 0 || month != 0 || days != 0) {
            messageD
        } else {
            val hourMinFormatter = DateTimeFormatter.ofPattern("HH:mm")
            return LocalDateTime.parse(messageD, formatter).format(hourMinFormatter)
        }
    } catch (e: Exception) {
        Log.d("showMessageSendTime", e.stackTraceToString())
        return ""
    }
}