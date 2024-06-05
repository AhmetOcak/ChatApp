package com.ahmetocak.chat_box.audio.player

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File
import javax.inject.Inject

class AndroidAudioPlayer @Inject constructor(
    private val context: Context
): AudioPlayer {

    private var player: MediaPlayer? = null

    override fun play(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
        }
    }

    override fun stop() {
        player?.apply {
            stop()
            release()
        }
        player = null
    }
}