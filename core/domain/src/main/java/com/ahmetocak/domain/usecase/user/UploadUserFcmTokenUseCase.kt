package com.ahmetocak.domain.usecase.user

import com.ahmetocak.common.Response
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.UiText
import com.ahmetocak.data.repository.user.UserRepository
import javax.inject.Inject

class UploadUserFcmTokenUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(email: String, token: String, onFailure: (UiText) -> Unit) {
        when (val response = userRepository.uploadUserFcmToken(email, token)) {
            is Response.Success -> {}
            is Response.Error -> onFailure(response.errorMessage)
        }
    }
}