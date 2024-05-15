package com.ahmetocak.data.mapper

import com.ahmetocak.model.Message
import com.ahmetocak.network.model.NetworkMessage

fun NetworkMessage.toMessage(): Message {
    return Message(
        authorId = senderId,
        authorName = senderName,
        authorImage = senderImage,
        message = content,
        time = time
    )
}

fun Message.toNetworkMessage(receiverId: String): NetworkMessage {
    return NetworkMessage(
        senderId = authorId,
        senderName = authorName,
        senderImage = authorImage,
        receiverId = receiverId,
        content = message,
        time = time
    )
}