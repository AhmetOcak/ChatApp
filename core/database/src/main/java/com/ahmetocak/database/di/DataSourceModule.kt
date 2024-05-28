package com.ahmetocak.database.di

import com.ahmetocak.database.dao.FriendDao
import com.ahmetocak.database.dao.UserDao
import com.ahmetocak.database.datasource.friend.FriendLocalDataSource
import com.ahmetocak.database.datasource.friend.FriendLocalDataSourceImpl
import com.ahmetocak.database.datasource.user.UserLocalDataSource
import com.ahmetocak.database.datasource.user.UserLocalDataSourceImpl
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
    fun provideUserLocalDataSource(userDao: UserDao): UserLocalDataSource {
        return UserLocalDataSourceImpl(userDao)
    }

    @Singleton
    @Provides
    fun provideFriendLocalDataSource(friendDao: FriendDao): FriendLocalDataSource {
        return FriendLocalDataSourceImpl(friendDao)
    }
}