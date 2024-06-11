package com.ahmetocak.domain.usecase.firebase.storage

import android.net.Uri
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

internal class UploadProfileImageUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {

    operator fun invoke(imageUri: Uri, onSuccess: (Uri) -> Unit, onFailure: (UiText) -> Unit) {
        storageRepository.uploadProfileImage(
            imageUri = imageUri,
            userUid = Firebase.auth.uid ?: ""
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.storage.downloadUrl.addOnCompleteListener { downloadUrlTask ->
                    if (downloadUrlTask.isSuccessful) {
                        onSuccess(downloadUrlTask.result)
                    } else {
                        onFailure(task.exception.failureMessage())
                    }
                }
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}