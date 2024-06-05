package com.ahmetocak.chat_box.audio.player

interface AudioPlayer {
    fun initializeMediaPlayer(onCompletion: () -> Unit)
    fun play(audioUrl: String)
    fun stop()
    fun releaseMediaPlayer()
}