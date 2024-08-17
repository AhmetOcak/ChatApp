package com.ahmetocak.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmetocak.database.dao.ChatGroupDao
import com.ahmetocak.database.dao.MessageDao
import com.ahmetocak.database.dao.RemoteKeyDao
import com.ahmetocak.database.dao.UserDao
import com.ahmetocak.database.entity.ChatGroupEntity
import com.ahmetocak.database.entity.ChatGroupParticipantsEntity
import com.ahmetocak.database.entity.MessageEntity
import com.ahmetocak.database.entity.RemoteKeyEntity
import com.ahmetocak.database.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ChatGroupEntity::class,
        ChatGroupParticipantsEntity::class,
        MessageEntity::class,
        RemoteKeyEntity::class
    ],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun chatGroupDao(): ChatGroupDao
    abstract fun messageDao(): MessageDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}