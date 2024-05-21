package com.ahmetocak.domain.usecase.user

import android.net.Uri
import com.ahmetocak.common.UiText
import com.ahmetocak.domain.usecase.firebase.storage.UploadProfileImageUseCase
import com.ahmetocak.domain.usecase.user.local.UpdateUserInCacheUseCase
import com.ahmetocak.domain.usecase.user.remote.UpdateUserInRemoteUseCase
import com.ahmetocak.model.User
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val updateUserInCacheUseCase: UpdateUserInCacheUseCase,
    private val updateUserInRemoteUseCase: UpdateUserInRemoteUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase
) {

    operator fun invoke(
        imageUri: String?,
        user: User,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        if (imageUri != null) {
            uploadProfileImageAndSync(
                user = user,
                onSuccess = onSuccess,
                onFailure = onFailure,
                imageUri = imageUri
            )
        } else {
            updateUserInRemoteAndCache(
                user = user,
                onSuccess = onSuccess,
                onFailure = onFailure
            )
        }
    }

    private fun uploadProfileImageAndSync(
        imageUri: String,
        user: User,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        uploadProfileImageUseCase(
            imageUri = Uri.parse(imageUri),
            onSuccess = { uri ->
                updateUserInRemoteAndCache(
                    user = user.copy(profilePicUrl = uri.toString()),
                    onSuccess = onSuccess,
                    onFailure = onFailure
                )
            },
            onFailure = onFailure
        )
    }

    private fun updateUserInRemoteAndCache(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        updateUserInRemoteUseCase(
            userEmail = user.email,
            username = user.username,
            profilePicUrl = user.profilePicUrl,
            onSuccess = {
                updateUserInCacheUseCase(user.copy(profilePicUrl = user.profilePicUrl))
                onSuccess()
            },
            onFailure = onFailure
        )
    }
}