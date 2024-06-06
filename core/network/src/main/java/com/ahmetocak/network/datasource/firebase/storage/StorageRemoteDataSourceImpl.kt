package com.ahmetocak.network.datasource.firebase.storage

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

private const val PROFILE_IMAGES_DIR = "user_profile_images"
private const val AUDIO_FILES_DIR = "user_audio_files"
private const val IMAGE_FILES_DIR = "user_image_files"

class StorageRemoteDataSourceImpl @Inject constructor(
    storage: FirebaseStorage
) : StorageRemoteDataSource {

    private val storageRef = storage.reference

    override fun uploadProfileImage(imageUri: Uri, userUid: String): UploadTask {
        val profileImageRef = storageRef.child("$PROFILE_IMAGES_DIR/$userUid")
        return profileImageRef.putFile(imageUri)
    }

    override fun getUserProfileImage(userUid: String): Task<Uri> {
        val profileImageRef = storageRef.child("$PROFILE_IMAGES_DIR/$userUid")
        return profileImageRef.downloadUrl
    }

    override fun deleteUserProfileImage(userUid: String): Task<Void> {
        val profileImageRef = storageRef.child("$PROFILE_IMAGES_DIR/$userUid")
        return profileImageRef.delete()
    }

    override fun uploadAudioFile(
        audioFileName: String,
        audioFileUri: Uri,
        userUid: String
    ): UploadTask {
        val audioFileRef = storageRef.child("$AUDIO_FILES_DIR/$userUid/$audioFileName")
        return audioFileRef.putFile(audioFileUri)
    }

    override fun uploadImageFile(
        imageFileName: String,
        imageFileUri: Uri,
        userUid: String
    ): UploadTask {
        val imageFileRef = storageRef.child("$IMAGE_FILES_DIR/$userUid/$imageFileName")
        return imageFileRef.putFile(imageFileUri)
    }
}