package com.ahmetocak.data.mapper

import com.ahmetocak.database.entity.ChatGroupEntity
import com.ahmetocak.database.entity.ChatGroupParticipantsEntity
import com.ahmetocak.model.ChatGroup
import com.ahmetocak.model.ChatGroupParticipants
import com.ahmetocak.model.GroupType
import com.ahmetocak.network.model.NetworkChatGroup
import com.ahmetocak.network.model.NetworkChatGroupParticipants
import java.lang.IllegalArgumentException

fun NetworkChatGroup.toChatGroup(): ChatGroup {
    return ChatGroup(
        id = id,
        name = name,
        imageUrl = imageUrl,
        participants = participants.map {
            ChatGroupParticipants(
                groupId = it.id,
                participantEmail = it.participantEmail,
                participantUsername = it.participantUsername,
                participantProfilePicUrl = it.participantProfilePicUrl
            )
        },
        groupType = groupType.toGroupType()
    )
}

fun List<NetworkChatGroup>.toListChatGroup(): List<ChatGroup> {
    return this.map { it.toChatGroup() }
}

fun NetworkChatGroup.toChatGroupEntity(): ChatGroupEntity {
    return ChatGroupEntity(
        groupId = id,
        name = name,
        groupType = groupType,
        groupImgUrl = imageUrl
    )
}

fun NetworkChatGroupParticipants.toParticipantsEntity() : ChatGroupParticipantsEntity {
    return ChatGroupParticipantsEntity(
        groupId = id,
        email = participantEmail,
        username = participantUsername,
        profileImgUrl = participantProfilePicUrl
    )
}

fun ChatGroupParticipantsEntity.toParticipants(): ChatGroupParticipants {
    return ChatGroupParticipants(
        groupId = groupId,
        participantUsername = username,
        participantEmail = email,
        participantProfilePicUrl = profileImgUrl
    )
}

fun String.toGroupType(): GroupType {
    return when (this) {
        GroupType.CHAT_GROUP.name -> GroupType.CHAT_GROUP
        GroupType.PRIVATE_CHAT_GROUP.name -> GroupType.PRIVATE_CHAT_GROUP
        else -> throw IllegalArgumentException("Wrong message type $this")
    }
}