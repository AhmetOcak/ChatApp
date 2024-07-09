package com.ahmetocak.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeyEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("friendship_id")
    val friendshipId: Int,

    @ColumnInfo("next_page")
    val nextPage: Int?
)
