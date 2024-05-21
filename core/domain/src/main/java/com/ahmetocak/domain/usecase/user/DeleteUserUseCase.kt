package com.ahmetocak.domain.usecase.user

import coil.ImageLoader
import com.ahmetocak.common.UiText
import com.ahmetocak.domain.usecase.auth.ReAuthenticateUseCase
import com.ahmetocak.domain.usecase.firebase.storage.DeleteUserProfileImageUseCase
import com.ahmetocak.domain.usecase.user.remote.DeleteAccountFromFirebaseUseCase
import com.ahmetocak.domain.usecase.user.local.DeleteUserFromCacheUseCase
import com.ahmetocak.domain.usecase.user.remote.DeleteUserFromRemoteUseCase
import com.ahmetocak.domain.usecase.utils.clearCoilCache
import com.ahmetocak.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val deleteUserFromCacheUseCase: DeleteUserFromCacheUseCase,
    private val deleteUserFromRemoteUseCase: DeleteUserFromRemoteUseCase,
    private val deleteAccountFromFirebaseUseCase: DeleteAccountFromFirebaseUseCase,
    private val deleteUserProfileImageUseCase: DeleteUserProfileImageUseCase,
    private val reAuthenticateUseCase: ReAuthenticateUseCase,
    private val imageLoader: ImageLoader
) {

    operator fun invoke(
        user: User,
        password: String = "",
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
                                    deleteUserFromCacheUseCase(user)
                                    deleteUserProfileImageUseCase(uid)
                                    clearCoilCache(imageLoader)
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