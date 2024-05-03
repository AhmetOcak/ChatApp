package com.ahmetocak.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Videocam

object ChatAppIcons {

    object Default {
        val arrowBack = Icons.AutoMirrored.Default.ArrowBack
        val send = Icons.AutoMirrored.Default.Send
    }

    object Filled {
        val attach = Icons.Filled.AttachFile
        val microphone = Icons.Filled.Mic
        val moreVert = Icons.Filled.MoreVert
    }

    object Outlined {
        val video = Icons.Outlined.Videocam
        val camera = Icons.Outlined.CameraAlt
        val call = Icons.Outlined.Call
        val edit = Icons.Outlined.Edit
        val person = Icons.Outlined.Person
        val info = Icons.Outlined.Info
    }
}