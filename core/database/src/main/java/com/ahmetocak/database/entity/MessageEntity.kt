package com.ahmetocak.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("sender_email")
    val senderEmail: String,

    @ColumnInfo("receiver_email")
    val receiverEmail: String,

    @ColumnInfo("message_content")
    val messageContent: String,

    @ColumnInfo("sent_at")
    val sentAt: String,

    @ColumnInfo("sender_img_url")
    val senderImgUrl: String?,

    @ColumnInfo("sender_username")
    val senderUsername: String,

    @ColumnInfo("message_type")
    val messageType: String
)
