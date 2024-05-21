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

class UpdateUserInCacheUseCase @Inject constructor(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(user: User) {
        CoroutineScope(dispatcher).launch {
            when (val response = repository.updateUserInCache(user)) {
                is Response.Success -> Log.d(USER_TAG, "User cache updated successfully.")
                is Response.Error -> Log.e(USER_TAG, response.errorMessage.toString())
            }
        }
    }
}