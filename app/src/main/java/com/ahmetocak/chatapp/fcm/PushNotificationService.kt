package com.ahmetocak.chatapp.fcm

import com.google.firebase.messaging.FirebaseMessagingService

class PushNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}