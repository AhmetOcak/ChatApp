package com.ahmetocak.chat_box.audio.recorder

interface AudioRecorder {
    fun startRecording(userEmail: String)
    fun stopRecording()
}