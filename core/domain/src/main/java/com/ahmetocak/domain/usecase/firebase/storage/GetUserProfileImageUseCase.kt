package com.ahmetocak.domain.usecase.firebase.storage

import android.net.Uri
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class GetUserProfileImageUseCase @Inject constructor(private val repository: StorageRepository) {

    operator fun invoke(onSuccess: (Uri) -> Unit, onFailure: (UiText) -> Unit) {
        repository.getUserProfileImage(
            userUid = Firebase.auth.uid ?: ""
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(task.result)
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}