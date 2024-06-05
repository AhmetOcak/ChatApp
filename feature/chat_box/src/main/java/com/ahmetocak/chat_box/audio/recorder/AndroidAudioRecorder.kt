package com.ahmetocak.chat_box.audio.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import javax.inject.Inject

class AndroidAudioRecorder @Inject constructor(
    private val context: Context
) : AudioRecorder {

    private var recorder: MediaRecorder? = null
    private lateinit var audioFile: File

    private fun createMediaRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun startRecording(userEmail: String) {
        audioFile = File(context.cacheDir, "$userEmail${LocalDateTime.now()}.mp3")

        createMediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(audioFile).fd)

            prepare()
            start()

            recorder = this
        }
    }

    override fun stopRecording(): File {
        recorder?.stop()
        recorder?.reset()
        recorder = null
        return audioFile
    }
}