package com.ahmetocak.domain.usecase.user.local

import com.ahmetocak.data.repository.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class ClearDbUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    operator fun invoke() {
        CoroutineScope(ioDispatcher).launch {
            userRepository.clearAllUserData()
        }
    }
}