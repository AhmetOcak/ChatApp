package com.ahmetocak.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("username")
    val username: String,

    @ColumnInfo("email")
    val email: String,

    @ColumnInfo("profilePicUrl")
    val profilePicUrl: String?
)
