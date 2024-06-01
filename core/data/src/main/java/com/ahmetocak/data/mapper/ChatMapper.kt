package com.ahmetocak.data.mapper

import com.ahmetocak.model.Message
import com.ahmetocak.network.model.NetworkMessage

internal fun Message.toNetworkMessage(): NetworkMessage {
    return NetworkMessage(
        id = id,
        senderEmail = senderEmail,
        receiverEmail = receiverEmail,
        messageText = messageText,
        sentAt = sentAt,
        senderImgUrl = senderImgUrl,
        senderUsername = senderUsername
    )
}