package com.ahmetocak.domain.usecase.auth

import coil.ImageLoader
import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.domain.usecase.user.local.ClearDbUseCase
import com.ahmetocak.domain.usecase.utils.clearCoilCache
import javax.inject.Inject

class SignOutUseCase @Inject internal constructor(
    private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient,
    private val clearDbUseCase: ClearDbUseCase,
    private val imageLoader: ImageLoader
) {

    operator fun invoke() {
        firebaseEmailPasswordClient.signOut()
        clearDbUseCase()
        clearCoilCache(imageLoader)
    }
}