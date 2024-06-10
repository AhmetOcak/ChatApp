package com.ahmetocak.common.ext

import android.util.Log
import android.util.Patterns
import com.ahmetocak.common.UiText
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
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

fun String?.encodeForSaveNav(): String? = if (this == null) null else URLEncoder.encode(
    this,
    StandardCharsets.UTF_8.toString()
)

fun String.showMessageTime(): String {
    return try {
        val currentDate = LocalDateTime.now()
        val messageDate = LocalDateTime.parse(this)
        val difference = ChronoUnit.DAYS.between(messageDate, currentDate)

        if (difference >= 1) {
            "${messageDate.hour.fixedTime()}:${messageDate.minute.fixedTime()} " +
                    "${messageDate.dayOfMonth.fixedTime()}-${messageDate.month.value.fixedTime()}-${messageDate.year.fixedTime()}"
        } else {
            "${messageDate.hour}:${messageDate.minute}"
        }
    } catch (e: Exception) {
        Log.d("showMessageTime", e.stackTraceToString())
        ""
    }
}

fun Int.fixedTime(): String {
    return if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
}

/*
fun OffsetDateTime.showMessageSendTime(): String {
    try {
        val currentDate = LocalDateTime.now()
        val difference = ChronoUnit.DAYS.between(this, currentDate)

        return if (difference > 1) {
            this.format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy"))
        } else {
            this.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
    } catch (e: Exception) {
        Log.d("showMessageSendTime", e.stackTraceToString())
        return ""
    }
}

fun String.toOffsetDateTime(): OffsetDateTime {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val dateTime = LocalDateTime.parse(this, formatter)
    return OffsetDateTime.of(dateTime, ZoneOffset.UTC)
}

 */