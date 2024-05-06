package com.ahmetocak.domain.usecase.auth

import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient) {

    operator fun invoke() = firebaseEmailPasswordClient.signOut()
}