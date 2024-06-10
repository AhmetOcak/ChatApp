package com.ahmetocak.domain.usecase.firebase.storage

import android.net.Uri
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

class UploadImageFileUseCase @Inject constructor(private val repository: StorageRepository) {

    operator fun invoke(
        imageFileUri: Uri,
        imageFileName: String,
        onSuccess: (Uri) -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        repository.uploadImageFile(
            imageFileName = imageFileName,
            imageFileUri = imageFileUri,
            userUid = Firebase.auth.uid ?: ""
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.storage.downloadUrl.addOnCompleteListener { downloadTask ->
                    if (downloadTask.isSuccessful) {
                        onSuccess(downloadTask.result)
                    } else onFailure(downloadTask.exception.failureMessage())
                }
            } else onFailure(task.exception.failureMessage())
        }
    }
}