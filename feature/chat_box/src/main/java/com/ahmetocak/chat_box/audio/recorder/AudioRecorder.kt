package com.ahmetocak.chat_box.audio.recorder

import java.io.File

interface AudioRecorder {
    fun startRecording(userEmail: String)
    fun stopRecording(): File
}