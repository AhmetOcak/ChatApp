package com.ahmetocak.chat_box.audio.player

import java.io.File

interface AudioPlayer {
    fun play(file: File)
    fun stop()
}