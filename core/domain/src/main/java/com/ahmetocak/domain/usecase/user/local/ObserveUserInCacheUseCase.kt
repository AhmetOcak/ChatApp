package com.ahmetocak.domain.usecase.user.local

import com.ahmetocak.data.repository.user.UserRepository
import javax.inject.Inject

class ObserveUserInCacheUseCase @Inject constructor(private val repository: UserRepository) {

    suspend operator fun invoke() = repository.observeUserInCache()
}