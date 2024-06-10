package com.ahmetocak.domain.usecase.firebase.storage

import android.net.Uri
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

class UploadDocFileUseCase @Inject constructor(private val repository: StorageRepository) {

    operator fun invoke(
        docFileName: String,
        docFileUri: Uri,
        onSuccess: (Uri) -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        repository.uploadDocFile(
            docFileName = docFileName,
            docFileUri = docFileUri,
            userUid = Firebase.auth.uid ?: ""
        ).addOnCompleteListener { task ->
            if (task.isComplete) {
                task.result.storage.downloadUrl.addOnCompleteListener { downloadTask ->
                    if (downloadTask.isSuccessful) {
                        onSuccess(downloadTask.result)
                    } else onFailure(task.exception.failureMessage())
                }
            } else onFailure(task.exception.failureMessage())
        }
    }
}