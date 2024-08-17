package com.ahmetocak.data.mapper

import com.ahmetocak.database.entity.MessageEntity
import com.ahmetocak.model.Message
import com.ahmetocak.model.MessageType
import com.ahmetocak.network.model.NetworkMessage
import java.lang.IllegalArgumentException

internal fun List<NetworkMessage>.toListMessageEntity(): List<MessageEntity> {
    return this.map {
        MessageEntity(
            id = it.id,
            messageBoxId = it.messageBoxId,
            senderEmail = it.senderEmail,
            messageContent = it.messageContent,
            sentAt = it.sentAt,
            senderImgUrl = it.senderImgUrl,
            senderUsername = it.senderUsername,
            messageType = it.messageType
        )
    }
}

internal fun MessageEntity.toMessage(): Message {
    return Message(
        id = id,
        messageBoxId = messageBoxId,
        senderEmail = senderEmail,
        messageContent = messageContent,
        sentAt = sentAt,
        senderImgUrl = senderImgUrl,
        senderUsername = senderUsername,
        messageType = messageType.toMessageType()
    )
}

internal fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        messageBoxId = messageBoxId,
        senderEmail = senderEmail,
        messageContent = messageContent,
        sentAt = sentAt,
        senderImgUrl = senderImgUrl,
        senderUsername = senderUsername,
        messageType = messageType.name
    )
}

internal fun Message.toNetworkMessage(): NetworkMessage {
    return NetworkMessage(
        id = id,
        messageBoxId = messageBoxId,
        senderEmail = senderEmail,
        messageContent = messageContent,
        sentAt = sentAt,
        senderImgUrl = senderImgUrl,
        senderUsername = senderUsername,
        messageType = messageType.name
    )
}

internal fun NetworkMessage.toMessage(): Message {
    return Message(
        id = id,
        messageBoxId = messageBoxId,
        senderEmail = senderEmail,
        messageContent = messageContent,
        sentAt = sentAt,
        senderImgUrl = senderImgUrl,
        senderUsername = senderUsername,
        messageType = messageType.toMessageType()
    )
}

private fun String.toMessageType(): MessageType {
    return when (this.uppercase()) {
        MessageType.TEXT.name -> MessageType.TEXT
        MessageType.AUDIO.name -> MessageType.AUDIO
        MessageType.IMAGE.name -> MessageType.IMAGE
        MessageType.DOC.name -> MessageType.DOC
        else -> throw IllegalArgumentException("Wrong message type $this")
    }
}