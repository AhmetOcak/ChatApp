package com.ahmetocak.domain.usecase.auth

import android.net.Uri
import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import javax.inject.Inject

class UpdateUserProfileImageUseCase @Inject constructor(private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient) {

    operator fun invoke(imageUri: Uri, onSuccess: () -> Unit, onFailure: (UiText) -> Unit) {
        firebaseEmailPasswordClient.updateUserProfileImage(imageUri)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}