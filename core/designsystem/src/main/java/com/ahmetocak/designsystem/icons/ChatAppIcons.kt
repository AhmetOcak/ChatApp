package com.ahmetocak.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Videocam

object ChatAppIcons {

    object Default {
        val arrowBack = Icons.AutoMirrored.Default.ArrowBack
        val send = Icons.AutoMirrored.Default.Send
        val logout = Icons.AutoMirrored.Default.Logout
    }

    object Filled {
        val attach = Icons.Filled.AttachFile
        val microphone = Icons.Filled.Mic
        val moreVert = Icons.Filled.MoreVert
        val email = Icons.Filled.Email
        val person = Icons.Filled.Person
        val delete = Icons.Filled.Delete
        val add = Icons.Filled.AddCircle
        val addPerson = Icons.Filled.PersonAddAlt1
        val chat = Icons.AutoMirrored.Filled.Chat
        val stories = Icons.Filled.Bookmarks
        val call = Icons.Filled.Call
        val settings = Icons.Filled.Settings
    }

    object Outlined {
        val video = Icons.Outlined.Videocam
        val camera = Icons.Outlined.CameraAlt
        val call = Icons.Outlined.Call
        val edit = Icons.Outlined.Edit
        val chat = Icons.AutoMirrored.Outlined.Chat
        val stories = Icons.Outlined.Bookmarks
        val settings = Icons.Outlined.Settings
    }
}