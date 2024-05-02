package com.ahmetocak.model

data class Call(
    val id: String,
    val imageUrl: String?,
    val title: String,
    val date: String,
    val callType: CallType,
    val callDirection: CallDirection,
    val isCallSuccessful: Boolean
)

enum class CallType {
    VOICE,
    VIDEO
}

enum class CallDirection {
    ONGOING,
    COMING
}