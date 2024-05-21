package com.ahmetocak.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmetocak.database.dao.UserDao
import com.ahmetocak.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}