package com.ahmetocak.domain.usecase.friend

import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.data.repository.friend.FriendRepository
import com.ahmetocak.model.Friend
import javax.inject.Inject

class CreateFriendUseCase @Inject constructor(private val friendRepository: FriendRepository) {

    suspend operator fun invoke(
        userEmail: String,
        friendEmail: String,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        when (val response = friendRepository.createFriend(
            userEmail = userEmail,
            friendEmail = friendEmail
        )) {
            is Response.Success -> cacheFriend(
                friend = response.data,
                onSuccess = onSuccess,
                onFailure = onFailure
            )

            is Response.Error -> onFailure(response.errorMessage)
        }
    }

    private suspend fun cacheFriend(
        friend: Friend,
        onSuccess: () -> Unit,
        onFailure: (UiText) -> Unit
    ) {
        when (val response = friendRepository.addFriendToCache(friend)) {
            is Response.Success -> onSuccess()
            is Response.Error -> onFailure(response.errorMessage)
        }
    }
}