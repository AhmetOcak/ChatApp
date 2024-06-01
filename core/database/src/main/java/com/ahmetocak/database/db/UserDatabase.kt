package com.ahmetocak.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmetocak.database.dao.FriendDao
import com.ahmetocak.database.dao.MessageDao
import com.ahmetocak.database.dao.RemoteKeyDao
import com.ahmetocak.database.dao.UserDao
import com.ahmetocak.database.entity.FriendEntity
import com.ahmetocak.database.entity.MessageEntity
import com.ahmetocak.database.entity.RemoteKeyEntity
import com.ahmetocak.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, FriendEntity::class, MessageEntity::class, RemoteKeyEntity::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun friendDao(): FriendDao
    abstract fun messageDao(): MessageDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}