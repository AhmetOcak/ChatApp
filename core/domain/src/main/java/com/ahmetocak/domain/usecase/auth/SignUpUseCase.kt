package com.ahmetocak.domain.usecase.auth

import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.ahmetocak.domain.usecase.user.local.AddUserToCacheUseCase
import com.ahmetocak.domain.usecase.user.remote.CreateUserUseCase
import javax.inject.Inject

class SignUpUseCase @Inject internal constructor(
    private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient,
    private val addUserToCacheUseCase: AddUserToCacheUseCase,
    private val createUserUseCase: CreateUserUseCase
) {

    operator fun invoke(
        username: String,
        email: String,
        password: String,
        profilePicUrl: String?,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        firebaseEmailPasswordClient.signUp(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                createUserUseCase(
                    email = email,
                    username = username,
                    profilePicUrl = profilePicUrl,
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