package com.ahmetocak.chat_box.di

import android.content.Context
import com.ahmetocak.chat_box.audio.player.AndroidAudioPlayer
import com.ahmetocak.chat_box.audio.player.AudioPlayer
import com.ahmetocak.chat_box.audio.recorder.AndroidAudioRecorder
import com.ahmetocak.chat_box.audio.recorder.AudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AudioModule {

    @Singleton
    @Provides
    fun provideAndroidAudioRecorder(@ApplicationContext context: Context): AudioRecorder {
        return AndroidAudioRecorder(context)
    }

    @Singleton
    @Provides
    fun provideAndroidPlayer(): AudioPlayer {
        return AndroidAudioPlayer()
    }
}