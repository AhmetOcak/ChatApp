package com.ahmetocak.network.datasource.firebase.storage

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

private const val DIRECTORY = "user_profile_images"

class StorageRemoteDataSourceImpl @Inject constructor(
    storage: FirebaseStorage
): StorageRemoteDataSource {

    private val storageRef = storage.reference

    override fun uploadProfileImage(imageUri: Uri, userUid: String): UploadTask {
        val profileImageRef = storageRef.child("$DIRECTORY/$userUid")
        return profileImageRef.putFile(imageUri)
    }

    override fun getUserProfileImage(userUid: String): Task<Uri> {
        val profileImageRef = storageRef.child("$DIRECTORY/$userUid")
        return profileImageRef.downloadUrl
    }

    override fun deleteUserProfileImage(userUid: String): Task<Void> {
        val profileImageRef = storageRef.child("$DIRECTORY/$userUid")
        return profileImageRef.delete()
    }
}