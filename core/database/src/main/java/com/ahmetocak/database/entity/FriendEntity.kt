package com.ahmetocak.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FriendEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("user_email")
    val userEmail: String?,

    @ColumnInfo("friend_email")
    val friendEmail: String?,

    @ColumnInfo("friend_username")
    val friendUsername: String?,

    @ColumnInfo("friend_profile_pic_url")
    val friendProfilePicUrl: String?
)
