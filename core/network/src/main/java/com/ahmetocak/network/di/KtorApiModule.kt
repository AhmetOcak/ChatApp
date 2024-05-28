package com.ahmetocak.network.di

import com.ahmetocak.network.helper.BASE_URL
import com.ahmetocak.network.api.KtorChatApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KtorApiModule {

    @Singleton
    @Provides
    fun provideKtorUserApi(): KtorChatApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KtorChatApi::class.java)
    }
}