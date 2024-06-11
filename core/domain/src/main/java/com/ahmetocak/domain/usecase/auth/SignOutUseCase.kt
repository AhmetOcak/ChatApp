package com.ahmetocak.domain.usecase.auth

import coil.ImageLoader
import com.ahmetocak.authentication.client.FirebaseEmailPasswordClient
import com.ahmetocak.domain.usecase.user.local.DeleteUserFromCacheUseCase
import com.ahmetocak.domain.usecase.utils.clearCoilCache
import com.ahmetocak.model.User
import javax.inject.Inject

class SignOutUseCase @Inject internal constructor(
    private val firebaseEmailPasswordClient: FirebaseEmailPasswordClient,
    private val deleteUserFromCacheUseCase: DeleteUserFromCacheUseCase,
    private val imageLoader: ImageLoader
) {

    operator fun invoke(user: User) {
        firebaseEmailPasswordClient.signOut()
        deleteUserFromCacheUseCase(user)
        clearCoilCache(imageLoader)
    }
}