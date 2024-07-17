package com.ahmetocak.database.di

import com.ahmetocak.database.dao.ChatGroupDao
import com.ahmetocak.database.dao.MessageDao
import com.ahmetocak.database.dao.UserDao
import com.ahmetocak.database.datasource.chat_group.ChatGroupLocalDataSource
import com.ahmetocak.database.datasource.chat_group.ChatGroupLocalDataSourceImpl
import com.ahmetocak.database.datasource.message.MessageLocalDataSource
import com.ahmetocak.database.datasource.message.MessageLocalDataSourceImpl
import com.ahmetocak.database.datasource.user.UserLocalDataSource
import com.ahmetocak.database.datasource.user.UserLocalDataSourceImpl
import com.ahmetocak.database.db.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideUserLocalDataSource(userDao: UserDao, db: UserDatabase): UserLocalDataSource {
        return UserLocalDataSourceImpl(userDao, db)
    }

    @Singleton
    @Provides
    fun provideChatGroupLocalDataSource(chatGroupDao: ChatGroupDao): ChatGroupLocalDataSource {
        return ChatGroupLocalDataSourceImpl(chatGroupDao)
    }

    @Singleton
    @Provides
    fun provideMessageLocalDataSource(messageDao: MessageDao): MessageLocalDataSource {
        return MessageLocalDataSourceImpl(messageDao)
    }
}