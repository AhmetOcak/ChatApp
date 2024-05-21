package com.ahmetocak.network.di

import com.ahmetocak.network.api.KtorUserApi
import com.ahmetocak.network.datasource.firebase.storage.StorageRemoteDataSource
import com.ahmetocak.network.datasource.firebase.storage.StorageRemoteDataSourceImpl
import com.ahmetocak.network.datasource.ktor_user.UserRemoteDataSource
import com.ahmetocak.network.datasource.ktor_user.UserRemoteDataSourceImpl
import com.google.firebase.storage.FirebaseStorage
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
    fun provideKtorUserDataSource(api: KtorUserApi): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(api)
    }

    @Singleton
    @Provides
    fun provideStorageRemoteDataSource(storage: FirebaseStorage): StorageRemoteDataSource {
        return StorageRemoteDataSourceImpl(storage)
    }
}