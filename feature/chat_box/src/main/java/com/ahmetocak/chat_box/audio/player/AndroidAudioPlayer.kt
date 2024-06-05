package com.ahmetocak.chat_box.audio.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.UiText
import javax.inject.Inject

class AndroidAudioPlayer @Inject constructor() : AudioPlayer {

    private val mediaPlayer: MediaPlayer = MediaPlayer()

    override fun initializeMediaPlayer(onCompletion: () -> Unit) {
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )

        mediaPlayer.setOnCompletionListener {
            stop()
            onCompletion()
        }
    }

    override fun play(audioUrl: String) {
        try {
            mediaPlayer.apply {
                stop()
                reset()
                setDataSource(audioUrl)
                prepare()
                start()
            }
        } catch (e: Exception) {
            SnackbarManager.showMessage(
                UiText.DynamicString(e.message ?: "Something went wrong !")
            )
            Log.e("AndroidAudioPlayer", e.stackTraceToString())
        }
    }

    override fun stop() {
        try {
            mediaPlayer.stop()
        } catch (e: Exception) {
            SnackbarManager.showMessage(
                UiText.DynamicString(e.message ?: "Something went wrong !")
            )
            Log.e("AndroidAudioPlayer", e.stackTraceToString())
        }
    }

    override fun releaseMediaPlayer() = mediaPlayer.release()
}