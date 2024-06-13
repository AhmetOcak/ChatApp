package com.ahmetocak.domain.usecase.user

import com.ahmetocak.common.UiText
import com.ahmetocak.domain.usecase.auth.ReAuthenticateUseCase
import com.ahmetocak.domain.usecase.firebase.storage.DeleteUserProfileImageUseCase
import com.ahmetocak.domain.usecase.user.local.ClearDbUseCase
import com.ahmetocak.domain.usecase.user.remote.DeleteAccountFromFirebaseUseCase
import com.ahmetocak.domain.usecase.user.remote.DeleteUserFromRemoteUseCase
import com.ahmetocak.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

class DeleteUserUseCase @Inject internal constructor(
    private val deleteUserFromRemoteUseCase: DeleteUserFromRemoteUseCase,
    private val deleteAccountFromFirebaseUseCase: DeleteAccountFromFirebaseUseCase,
    private val deleteUserProfileImageUseCase: DeleteUserProfileImageUseCase,
    private val clearDbUseCase: ClearDbUseCase,
    private val reAuthenticateUseCase: ReAuthenticateUseCase,
) {

    operator fun invoke(
        user: User,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        val uid = Firebase.auth.currentUser?.uid

        if (uid != null) {
            reAuthenticateUseCase(
                email = user.email,
                password = password,
                onFailure = onFailure,
                onSuccess = {
                    deleteAccountFromFirebaseUseCase(
                        onFailure = onFailure,
                        onSuccess = {
                            deleteUserFromRemoteUseCase(
                                userEmail = user.email,
                                onSuccess = {
                                    deleteUserProfileImageUseCase(uid)
                                    clearDbUseCase()
                                    onSuccess()
                                },
                                onFailure = onFailure
                            )
                        }
                    )
                }
            )
        }
    }
}