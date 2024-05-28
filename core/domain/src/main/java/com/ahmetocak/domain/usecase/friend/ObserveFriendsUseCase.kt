package com.ahmetocak.domain.usecase.friend

import com.ahmetocak.data.repository.friend.FriendRepository
import javax.inject.Inject

class ObserveFriendsUseCase @Inject constructor(private val repository: FriendRepository) {

    suspend operator fun invoke() = repository.observeFriendInCache()
}