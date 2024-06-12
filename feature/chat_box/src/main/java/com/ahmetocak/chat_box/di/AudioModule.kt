package com.ahmetocak.chat_box.di

import android.content.Context
import com.ahmetocak.chat_box.audio.player.AndroidAudioPlayer
import com.ahmetocak.chat_box.audio.player.AudioPlayer
import com.ahmetocak.chat_box.audio.recorder.AndroidAudioRecorder
import com.ahmetocak.chat_box.audio.recorder.AudioRecorder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
internal object AudioModule {

    @Provides
    fun provideAndroidAudioRecorder(@ApplicationContext context: Context): AudioRecorder {
        return AndroidAudioRecorder(context)
    }

    @Provides
    fun provideAndroidPlayer(): AudioPlayer {
        return AndroidAudioPlayer()
    }
}