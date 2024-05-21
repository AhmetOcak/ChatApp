package com.ahmetocak.domain.usecase.auth

import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.authentication.client.GoogleSignInClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class ReAuthenticateUseCase @Inject constructor(
    private val firebaseAuthClient: FirebaseEmailPasswordClient,
    private val googleAuthClient: GoogleSignInClient
) {
    operator fun invoke(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        val signInProvider = FirebaseAuth.getInstance().getAccessToken(false).result.signInProvider

        if (signInProvider == GoogleAuthProvider.PROVIDER_ID) {
            googleAuthClient.reAuthenticate(
                onSuccess = onSuccess,
                onFailure = { onFailure(it.failureMessage()) }
            )
        } else {
            firebaseAuthClient.reAuthenticate(email, password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception.failureMessage())
                }
            }
        }
    }
}