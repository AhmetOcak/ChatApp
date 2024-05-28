package com.ahmetocak.domain.usecase.firebase.storage

import android.net.Uri
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class GetUserProfileImageUseCase @Inject constructor(private val repository: StorageRepository) {

    operator fun invoke(onResult: (Uri?) -> Unit) {
        repository.getUserProfileImage(
            userUid = Firebase.auth.uid ?: ""
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(task.result)
            } else {
                onResult(null)
            }
        }
    }
}