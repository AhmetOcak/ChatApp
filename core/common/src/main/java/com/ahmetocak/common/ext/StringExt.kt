package com.ahmetocak.common.ext

import android.util.Patterns
import com.ahmetocak.common.UiText
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