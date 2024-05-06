package com.ahmetocak.domain.usecase.auth

import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient,
    private val updateUsernameUseCase: UpdateUsernameUseCase
) {

    operator fun invoke(
        name: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        firebaseEmailPasswordClient.signUp(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                updateUsernameUseCase(
                    name = name,
                    onSuccess = onSuccess,
                    onFailure = onFailure
                )
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}