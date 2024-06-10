package com.ahmetocak.domain.usecase.firebase.storage

import android.net.Uri
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import javax.inject.Inject

class UploadAudioFileUseCase @Inject constructor(private val repository: StorageRepository) {

    operator fun invoke(
        audioFileName: String,
        audioFileUri: Uri,
        onSuccess: (Uri) -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        repository.uploadAudioFile(
            userUid = Firebase.auth.uid ?: "",
            audioFileName = audioFileName,
            audioFileUri = audioFileUri
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result.storage.downloadUrl.addOnCompleteListener { downloadTask ->
                    if (downloadTask.isSuccessful) {
                        onSuccess(downloadTask.result)
                    } else onFailure(task.exception.failureMessage())
                }
            } else onFailure(task.exception.failureMessage())
        }
    }
}