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
            senderEmail = it.senderEmail,
            receiverEmail = it.receiverEmail,
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
        senderEmail = senderEmail,
        receiverEmail = receiverEmail,
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
        senderEmail = senderEmail,
        receiverEmail = receiverEmail,
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
        senderEmail = senderEmail,
        receiverEmail = receiverEmail,
        messageContent = messageContent,
        sentAt = sentAt,
        senderImgUrl = senderImgUrl,
        senderUsername = senderUsername,
        messageType = messageType.name
    )
}

private fun String.toMessageType(): MessageType {
    return when (this.uppercase()) {
        MessageType.TEXT.name -> MessageType.TEXT
        MessageType.AUDIO.name -> MessageType.AUDIO
        else -> throw IllegalArgumentException("Wrong message type $this")
    }
}