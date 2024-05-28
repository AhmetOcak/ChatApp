package com.ahmetocak.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FriendEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("userEmail")
    val userEmail: String,

    @ColumnInfo("friendEmail")
    val friendEmail: String,

    @ColumnInfo("friendProfilePicUrl")
    val friendProfilePicUrl: String?
)
