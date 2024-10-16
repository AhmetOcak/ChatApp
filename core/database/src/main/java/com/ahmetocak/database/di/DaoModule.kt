package com.ahmetocak.database.di

import com.ahmetocak.database.dao.ChatGroupDao
import com.ahmetocak.database.dao.MessageDao
import com.ahmetocak.database.dao.RemoteKeyDao
import com.ahmetocak.database.dao.UserDao
import com.ahmetocak.database.db.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Singleton
    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideChatGroupDao(userDatabase: UserDatabase): ChatGroupDao {
        return userDatabase.chatGroupDao()
    }

    @Singleton
    @Provides
    fun provideMessageDao(userDatabase: UserDatabase): MessageDao {
        return userDatabase.messageDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeyDao(userDatabase: UserDatabase): RemoteKeyDao {
        return userDatabase.remoteKeyDao()
    }
}