package com.ahmetocak.database.di

import com.ahmetocak.database.dao.UserDao
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
}