package com.ahmetocak.domain.usecase.chat_group

import android.net.Uri
import com.ahmetocak.common.Response
import com.ahmetocak.common.UiText
import com.ahmetocak.data.repository.chat_group.ChatGroupRepository
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateChatGroupImageUseCase @Inject constructor(
    private val chatGroupRepository: ChatGroupRepository,
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(
        imageUrl: Uri,
        groupId: Int,
        imageFileName: String,
        onFailure: (UiText) -> Unit,
        onSuccess: (Uri) -> Unit
    ) {
        val groupImgStorageUrl = storageRepository.uploadChatGroupImage(
            imageFileName = imageFileName,
            imageFileUri = imageUrl
        ).await().storage.downloadUrl.await()


        if (groupImgStorageUrl == null) {
            onFailure(UiText.DynamicString("Image upload failed."))
            return
        }

        when (val response = chatGroupRepository.updateGroupImage(groupImgStorageUrl.toString(), groupId)) {
            is Response.Success -> onSuccess(groupImgStorageUrl)
            is Response.Error -> onFailure(response.errorMessage)
        }
    }
}