package com.ahmetocak.domain.usecase.user.remote

import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.data.repository.user.UserRepository
import com.ahmetocak.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        username: String,
        email: String,
        profilePicUrl: String?,
        onSuccess: (User) -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            val response = userRepository.create(
                email = email,
                username = username,
                profilePicUrl = profilePicUrl
            )
            when (response) {
                is Response.Success -> onSuccess(response.data)
                is Response.Error -> onFailure(response.errorMessage)
            }
        }
    }
}