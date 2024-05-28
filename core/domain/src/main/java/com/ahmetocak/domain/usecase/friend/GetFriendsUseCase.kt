package com.ahmetocak.domain.usecase.friend

import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.data.repository.friend.FriendRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val friendRepository: FriendRepository,
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(
        userEmail: String,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        CoroutineScope(dispatcher).launch {
            when (val response = friendRepository.getFriends(userEmail)) {
                is Response.Success -> {
                    response.data.forEach { friend ->
                        friendRepository.addFriendToCache(friend)
                    }
                    onSuccess()
                }

                is Response.Error -> onFailure(response.errorMessage)
            }
        }
    }
}