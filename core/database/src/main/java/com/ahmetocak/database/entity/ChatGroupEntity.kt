package com.ahmetocak.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class ChatGroupEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("group_id")
    val groupId: Int,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("group_img_url")
    val groupImgUrl: String?,

   @ColumnInfo("group_type")
    val groupType: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = ChatGroupEntity::class,
        parentColumns = ["group_id"],
        childColumns = ["group_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["group_id", "email"], unique = true)]
)
data class ChatGroupParticipantsEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo("group_id")
    val groupId: Int,

    @ColumnInfo("email")
    val email: String,

    @ColumnInfo("username")
    val username: String,

    @ColumnInfo("profile_img_url")
    val profileImgUrl: String?
)
