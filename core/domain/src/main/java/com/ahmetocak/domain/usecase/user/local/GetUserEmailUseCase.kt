package com.ahmetocak.domain.usecase.user.local

import com.ahmetocak.data.repository.user.UserRepository
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.getUserEmail()
}