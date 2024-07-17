package com.ahmetocak.data.repository.chat_group

import com.ahmetocak.common.Response
import com.ahmetocak.model.ChatGroup
import kotlinx.coroutines.flow.Flow

interface ChatGroupRepository {
    suspend fun createPrivateGroup(
        currentUserEmail: String,
        friendEmail: String
    ): Response<Unit>
    suspend fun createGroup(
        creatorEmail: String,
        groupName: String,
        creatorUsername: String,
        creatorProfilePicUrl: String?,
        groupImageUrl: String?
    ): Response<Unit>
    suspend fun getGroups(userEmail: String): Response<Unit>
    suspend fun observeGroups(): Flow<List<ChatGroup>>
}