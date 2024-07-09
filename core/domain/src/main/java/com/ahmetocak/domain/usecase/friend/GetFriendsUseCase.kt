package com.ahmetocak.domain.usecase.friend

import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.data.repository.friend.FriendRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(private val friendRepository: FriendRepository) {

    suspend operator fun invoke(
        userEmail: String,
        onComplete: suspend (UiText?) -> Unit
    ) {
        when (val response = friendRepository.getFriends(userEmail)) {
            is Response.Success -> {
                response.data.forEach { friend ->
                    friendRepository.addFriendToCache(friend)
                }
                onComplete(null)
            }

            is Response.Error -> onComplete(response.errorMessage)
        }
    }
}