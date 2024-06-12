package com.ahmetocak.domain.usecase.user.remote

import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.data.repository.user.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DeleteUserFromRemoteUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        userEmail: String,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            when (val response = repository.deleteUser(userEmail)) {
                is Response.Success -> onSuccess()
                is Response.Error -> onFailure(response.errorMessage)
            }
        }
    }
}