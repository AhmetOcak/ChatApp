package com.ahmetocak.data.mapper

import com.ahmetocak.database.entity.MessageEntity
import com.ahmetocak.model.Message
import com.ahmetocak.network.model.NetworkMessage

fun List<NetworkMessage>.toListMessageEntity(): List<MessageEntity> {
    return this.map {
        MessageEntity(
            id = it.id,
            senderEmail = it.senderEmail,
            receiverEmail = it.receiverEmail,
            messageText = it.messageText,
            sentAt = it.sentAt,
            senderImgUrl = it.senderImgUrl,
            senderUsername = it.senderUsername
        )
    }
}

fun MessageEntity.toMessage(): Message {
    return Message(
        id = id,
        senderEmail = senderEmail,
        receiverEmail = receiverEmail,
        messageText = messageText,
        sentAt = sentAt,
        senderImgUrl = senderImgUrl,
        senderUsername = senderUsername
    )
}

fun Message.toMessageEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        senderEmail = senderEmail,
        receiverEmail = receiverEmail,
        messageText = messageText,
        sentAt = sentAt,
        senderImgUrl = senderImgUrl,
        senderUsername = senderUsername
    )
}
