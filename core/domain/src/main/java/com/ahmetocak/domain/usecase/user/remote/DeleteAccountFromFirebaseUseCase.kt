package com.ahmetocak.domain.usecase.user.remote

import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.common.UiText
import com.ahmetocak.common.ext.failureMessage
import javax.inject.Inject

internal class DeleteAccountFromFirebaseUseCase @Inject constructor(
    private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient
) {

    operator fun invoke(onSuccess: () -> Unit, onFailure: (UiText) -> Unit) {
        firebaseEmailPasswordClient.deleteAccount()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception.failureMessage())
            }
        }
    }
}