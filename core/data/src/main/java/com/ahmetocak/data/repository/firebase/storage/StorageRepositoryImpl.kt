package com.ahmetocak.data.repository.firebase.storage

import android.net.Uri
import com.ahmetocak.network.datasource.firebase.storage.StorageRemoteDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storageRemoteDataSource: StorageRemoteDataSource
) : StorageRepository {
    override fun uploadProfileImage(imageUri: Uri, userUid: String): UploadTask {
        return storageRemoteDataSource.uploadProfileImage(imageUri, userUid)
    }

    override fun getUserProfileImage(userUid: String): Task<Uri> {
        return storageRemoteDataSource.getUserProfileImage(userUid)
    }

    override fun deleteUserProfileImage(userUid: String): Task<Void> {
        return storageRemoteDataSource.deleteUserProfileImage(userUid)
    }

    override fun uploadAudioFile(
        audioFileName: String,
        audioFileUri: Uri,
        userUid: String
    ): UploadTask {
        return storageRemoteDataSource.uploadAudioFile(
            audioFileName = audioFileName,
            audioFileUri = audioFileUri,
            userUid = userUid
        )
    }
}