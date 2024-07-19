package com.ahmetocak.network.datasource.ktor_chat_group

import com.ahmetocak.common.Response
import com.ahmetocak.network.model.NetworkChatGroup
import com.ahmetocak.network.model.NetworkUser

interface ChatGroupRemoteDataSource {
    suspend fun createPrivateGroup(
        currentUserEmail: String,
        friendEmail: String
    ): Response<NetworkChatGroup>

    suspend fun createGroup(
        creatorEmail: String,
        groupName: String,
        creatorUsername: String,
        creatorProfilePicUrl: String?,
        groupImageUrl: String?,
    ): Response<NetworkChatGroup>

    suspend fun getGroups(userEmail: String): Response<List<NetworkChatGroup>>
    suspend fun addParticipantToChatGroup(
        groupId: Int,
        participantEmail: String
    ): Response<NetworkUser>
}