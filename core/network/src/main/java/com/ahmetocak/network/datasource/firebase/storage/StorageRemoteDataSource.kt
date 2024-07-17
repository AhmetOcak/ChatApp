package com.ahmetocak.network.datasource.firebase.storage

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask

interface StorageRemoteDataSource {
    fun uploadProfileImage(imageUri: Uri, userUid: String): UploadTask
    fun getUserProfileImage(userUid: String): Task<Uri>
    fun deleteUserProfileImage(userUid: String): Task<Void>
    fun uploadAudioFile(audioFileName: String, audioFileUri: Uri, userUid: String): UploadTask
    fun uploadImageFile(imageFileName: String, imageFileUri: Uri, userUid: String): UploadTask
    fun uploadDocFile(docFileName: String, docFileUri: Uri, userUid: String): UploadTask
    fun uploadChatGroupImage(imageFileName: String, imageFileUri: Uri): UploadTask
}