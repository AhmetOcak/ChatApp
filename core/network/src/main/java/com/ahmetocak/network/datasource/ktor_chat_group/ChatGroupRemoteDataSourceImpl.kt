package com.ahmetocak.network.datasource.ktor_chat_group

import com.ahmetocak.common.Response
import com.ahmetocak.network.api.KtorChatApi
import com.ahmetocak.network.helper.apiCall
import com.ahmetocak.network.model.NetworkChatGroup
import javax.inject.Inject

class ChatGroupRemoteDataSourceImpl @Inject constructor(
    private val api: KtorChatApi
) : ChatGroupRemoteDataSource {
    override suspend fun createPrivateGroup(
        currentUserEmail: String,
        friendEmail: String
    ): Response<NetworkChatGroup> {
        return apiCall { api.createPrivateGroup(currentUserEmail, friendEmail) }
    }

    override suspend fun createGroup(
        creatorEmail: String,
        groupName: String,
        creatorUsername: String,
        creatorProfilePicUrl: String?,
        groupImageUrl: String?
    ): Response<NetworkChatGroup> {
        return apiCall {
            api.createGroup(
                creatorEmail = creatorEmail,
                groupName = groupName,
                creatorUsername = creatorUsername,
                creatorProfilePicUrl = creatorProfilePicUrl,
                groupImageUrl = groupImageUrl
            )
        }
    }

    override suspend fun getGroups(userEmail: String): Response<List<NetworkChatGroup>> {
        return apiCall { api.getGroups(userEmail) }
    }

    override suspend fun addParticipantToChatGroup(
        groupId: Int,
        participantEmail: String,
        participantUsername: String,
        participantProfilePicUrl: String?
    ): Response<Unit> {
        return apiCall {
            api.addParticipantToChatGroup(
                groupId = groupId,
                participantUsername = participantUsername,
                participantEmail = participantEmail,
                participantProfilePicUrl = participantProfilePicUrl
            )
        }
    }
}