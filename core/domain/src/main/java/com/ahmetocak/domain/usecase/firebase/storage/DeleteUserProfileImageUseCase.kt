package com.ahmetocak.domain.usecase.firebase.storage

import android.util.Log
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import javax.inject.Inject

internal class DeleteUserProfileImageUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {

    operator fun invoke(userUid: String) {
        storageRepository.deleteUserProfileImage(userUid).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("STORAGE", "User profile image deleted from storage.")
            } else {
                Log.e("STORAGE", task.exception?.stackTraceToString() ?: task.result.toString())
            }
        }
    }
}