package com.ahmetocak.designsystem.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Edit

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
        val settings = Icons.Filled.Settings
        val stop = Icons.Filled.Stop
        val play = Icons.Filled.PlayArrow
        val gallery = Icons.Filled.PhotoLibrary
        val document = Icons.Filled.Description
        val location = Icons.Filled.LocationOn
        val capture = Icons.Filled.Circle
        val darkMode = Icons.Filled.DarkMode
        val brightnessAuto = Icons.Filled.BrightnessAuto
        val account = Icons.Filled.AccountCircle
        val edit = Icons.Filled.Edit
    }

    object Outlined {
        val camera = Icons.Outlined.CameraAlt
    }
}