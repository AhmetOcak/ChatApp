package com.ahmetocak.data.repository.chat_group

import com.ahmetocak.common.Response
import com.ahmetocak.data.mapper.toChatGroupEntity
import com.ahmetocak.data.mapper.toGroupType
import com.ahmetocak.data.mapper.toParticipants
import com.ahmetocak.data.mapper.toParticipantsEntity
import com.ahmetocak.database.datasource.chat_group.ChatGroupLocalDataSource
import com.ahmetocak.database.entity.ChatGroupParticipantsEntity
import com.ahmetocak.model.ChatGroup
import com.ahmetocak.model.ChatGroupParticipants
import com.ahmetocak.model.GroupType
import com.ahmetocak.network.datasource.ktor_chat_group.ChatGroupRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ChatGroupRepositoryImpl @Inject constructor(
    private val remoteDataSource: ChatGroupRemoteDataSource,
    private val localDataSource: ChatGroupLocalDataSource
) : ChatGroupRepository {
    override suspend fun createPrivateGroup(
        currentUserEmail: String,
        friendEmail: String
    ): Response<Unit> {
        return when (val response =
            remoteDataSource.createPrivateGroup(currentUserEmail, friendEmail)) {
            is Response.Success -> {
                val data = response.data

                localDataSource.insertChatGroup(data.toChatGroupEntity())
                data.participants.map {
                    localDataSource.insertChatGroupParticipants(it.toParticipantsEntity())
                }

                Response.Success(Unit)
            }

            is Response.Error -> Response.Error(response.errorMessage)
        }
    }

    override suspend fun createGroup(
        creatorEmail: String,
        groupName: String,
        creatorUsername: String,
        creatorProfilePicUrl: String?,
        groupImageUrl: String?,
        participants: List<ChatGroupParticipants>
    ): Response<Unit> {
        return when (val response = remoteDataSource.createGroup(
            creatorEmail = creatorEmail,
            groupName = groupName,
            creatorUsername = creatorUsername,
            creatorProfilePicUrl = creatorProfilePicUrl,
            groupImageUrl = groupImageUrl
        )) {
            is Response.Success -> {
                val data = response.data
                if (data.groupType == GroupType.PRIVATE_CHAT_GROUP.name) {
                    localDataSource.insertChatGroup(
                        data.copy(
                            imageUrl = data.participants.filterNot {
                                it.participantEmail == creatorEmail
                            }.first().participantProfilePicUrl
                        ).toChatGroupEntity()
                    )
                } else {
                    localDataSource.insertChatGroup(data.toChatGroupEntity())
                }
                data.participants.map {
                    localDataSource.insertChatGroupParticipants(it.toParticipantsEntity())
                }

                if (participants.isNotEmpty()) {
                    participants.forEach {
                        addParticipantToChatGroup(
                            groupId = data.id,
                            participantEmail = it.participantEmail
                        )
                    }
                }

                Response.Success(Unit)
            }

            is Response.Error -> Response.Error(response.errorMessage)
        }
    }

    override suspend fun getGroups(userEmail: String): Response<Unit> {
        return when (val response = remoteDataSource.getGroups(userEmail)) {
            is Response.Success -> {
                response.data.forEach { data ->
                    if (data.groupType == GroupType.PRIVATE_CHAT_GROUP.name) {
                        localDataSource.insertChatGroup(
                            data.copy(
                                imageUrl = data.participants.filterNot {
                                    it.participantEmail == userEmail
                                }.first().participantProfilePicUrl
                            ).toChatGroupEntity()
                        )
                    } else {
                        localDataSource.insertChatGroup(data.toChatGroupEntity())
                    }

                    data.participants.map {
                        localDataSource.insertChatGroupParticipants(it.toParticipantsEntity())
                    }
                }
                Response.Success(Unit)
            }

            is Response.Error -> Response.Error(response.errorMessage)
        }
    }

    override suspend fun observeGroups(): Flow<List<ChatGroup>> {
        return localDataSource.getAllChatGroups()
            .combine(localDataSource.getChatGroupParticipants()) { group, participant ->
                group.map {
                    ChatGroup(
                        id = it.groupId,
                        name = it.name,
                        groupType = it.groupType.toGroupType(),
                        imageUrl = it.groupImgUrl,
                        participants = participant.map { p -> p.toParticipants() }
                    )
                }
            }
    }

    override suspend fun addParticipantToChatGroup(
        groupId: Int,
        participantEmail: String
    ): Response<Unit> {
        return when (val response =
            remoteDataSource.addParticipantToChatGroup(groupId, participantEmail)) {
            is Response.Success -> {
                with(response.data) {
                    localDataSource.insertChatGroupParticipants(
                        chatGroupParticipantsEntity = ChatGroupParticipantsEntity(
                            groupId = groupId,
                            username = username,
                            email = email,
                            profileImgUrl = profilePicUrl
                        )
                    )
                }
                return Response.Success(Unit)
            }

            is Response.Error -> Response.Error(response.errorMessage)
        }
    }

    override suspend fun updateGroupImage(imgUrl: String, groupId: Int): Response<Unit> {
        return remoteDataSource.updateGroupImage(imgUrl = imgUrl, groupId = groupId)
    }
}