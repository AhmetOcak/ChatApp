package com.ahmetocak.domain.usecase.auth

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.ahmetocak.authentication.client.GoogleSignInClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(private val googleSignInClient: GoogleSignInClient) {

    fun startSignInIntent(
        onFailure: (UiText) -> Unit,
        onSuccess: (IntentSenderRequest) -> Unit
    ) {
        googleSignInClient.startSignInIntent().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(
                    IntentSenderRequest.Builder(
                        task.result.pendingIntent.intentSender
                    ).build()
                )
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }

    fun signInWithGoogle(
        intent: Intent,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        googleSignInClient.signInWithIntent(intent).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}