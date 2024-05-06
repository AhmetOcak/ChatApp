package com.ahmetocak.domain.usecase.auth

import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient) {

    operator fun invoke(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        firebaseEmailPasswordClient.login(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}