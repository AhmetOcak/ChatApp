package com.ahmetocak.domain.usecase.auth

import android.content.Intent
import androidx.activity.result.IntentSenderRequest
import com.ahmetocak.authentication.client.GoogleSignInClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.ahmetocak.domain.usecase.user.local.AddUserToCacheUseCase
import com.ahmetocak.domain.usecase.user.remote.CreateUserUseCase
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val googleSignInClient: GoogleSignInClient,
    private val addUserToCacheUseCase: AddUserToCacheUseCase,
    private val createUserUseCase: CreateUserUseCase
) {

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
                val result = task.result.user

                createUserUseCase(
                    email = result?.email!!,
                    username = result.email!!,
                    profilePicUrl = result.photoUrl.toString(),
                    onSuccess = { user ->
                        addUserToCacheUseCase(user)
                        onSuccess()
                    },
                    onFailure = onFailure
                )
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}