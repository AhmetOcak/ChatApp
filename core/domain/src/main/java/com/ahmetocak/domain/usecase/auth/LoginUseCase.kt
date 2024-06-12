package com.ahmetocak.domain.usecase.auth

import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.ahmetocak.domain.usecase.firebase.storage.GetUserProfileImageUseCase
import com.ahmetocak.domain.usecase.friend.GetFriendsUseCase
import com.ahmetocak.domain.usecase.user.local.AddUserToCacheUseCase
import com.ahmetocak.domain.usecase.user.remote.GetUserFromRemoteUseCase
import javax.inject.Inject

class LoginUseCase @Inject internal constructor(
    private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient,
    private val getUserFromRemoteUseCase: GetUserFromRemoteUseCase,
    private val addUserToCacheUseCase: AddUserToCacheUseCase,
    private val getUserProfileImageUseCase: GetUserProfileImageUseCase,
    private val getFriendsUseCase: GetFriendsUseCase
) {

    operator fun invoke(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        firebaseEmailPasswordClient.login(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                getUserProfileImageUseCase(
                    onResult = { uri ->
                        getUserFromRemoteUseCase(
                            userEmail = email,
                            onFailure = onFailure,
                            onSuccess = { user ->
                                addUserToCacheUseCase(user.copy(profilePicUrl = uri.toString()))

                                getFriendsUseCase(
                                    userEmail = email,
                                    onSuccess = onSuccess,
                                    onFailure = onFailure
                                )
                            }
                        )
                    }
                )
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}