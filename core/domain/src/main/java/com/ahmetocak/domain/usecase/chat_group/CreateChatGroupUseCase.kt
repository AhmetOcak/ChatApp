package com.ahmetocak.domain.usecase.chat_group

import android.net.Uri
import com.ahmetocak.common.Response
import com.ahmetocak.data.repository.chat_group.ChatGroupRepository
import com.ahmetocak.data.repository.firebase.storage.StorageRepository
import com.ahmetocak.domain.usecase.firebase.storage.UploadImageFileUseCase
import com.ahmetocak.model.ChatGroupParticipants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import javax.inject.Inject

class CreateChatGroupUseCase @Inject constructor(
    private val repository: ChatGroupRepository,
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(
        creatorEmail: String,
        groupName: String,
        creatorUsername: String,
        creatorProfilePicUrl: String?,
        groupImageUrl: Uri?,
        participants: List<ChatGroupParticipants>
    ): Response<Unit> {
        val url = if (groupImageUrl != null) {
            storageRepository.uploadImageFile(
                imageFileName = "$groupName-${LocalDateTime.now()}",
                imageFileUri = groupImageUrl,
                userUid = Firebase.auth.uid ?: ""
            ).await().storage.downloadUrl.await().toString()
        } else null

        return repository.createGroup(
            creatorEmail = creatorEmail,
            groupName = groupName,
            creatorUsername = creatorUsername,
            creatorProfilePicUrl = creatorProfilePicUrl,
            groupImageUrl = url,
            participants = participants
        )
    }
}