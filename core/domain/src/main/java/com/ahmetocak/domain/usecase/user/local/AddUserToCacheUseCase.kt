package com.ahmetocak.domain.usecase.user.local

import android.util.Log
import com.ahmetocak.common.Response
import com.ahmetocak.data.repository.user.UserRepository
import com.ahmetocak.domain.usecase.utils.USER_TAG
import com.ahmetocak.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class AddUserToCacheUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(user: User) {
        CoroutineScope(dispatcher).launch {
            when (val response = userRepository.addUserToCache(user)) {
                is Response.Success -> Log.d(USER_TAG, "User cached successfully.")
                is Response.Error -> Log.e(USER_TAG, response.errorMessage.toString())
            }
        }
    }
}