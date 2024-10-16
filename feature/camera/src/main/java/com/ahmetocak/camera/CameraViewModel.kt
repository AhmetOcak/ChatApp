package com.ahmetocak.camera

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.camera.capture_img.CameraController
import com.ahmetocak.camera.navigation.MESSAGE_BOX_ID
import com.ahmetocak.camera.navigation.SENDER_EMAIL
import com.ahmetocak.camera.navigation.SENDER_IMG_URL
import com.ahmetocak.camera.navigation.SENDER_USERNAME
import com.ahmetocak.common.SnackbarManager
import com.ahmetocak.common.UiText
import com.ahmetocak.domain.usecase.chat.SendMessageUseCase
import com.ahmetocak.domain.usecase.firebase.storage.UploadImageFileUseCase
import com.ahmetocak.model.Message
import com.ahmetocak.model.MessageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val sendMessageUseCase: SendMessageUseCase,
    private val uploadImageFileUseCase: UploadImageFileUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private var messageBoxId: Int? = null
    private var senderEmail: String? = null
    private var senderImgUrl: String? = null
    private var senderUsername: String? = null

    init {
        messageBoxId = savedStateHandle.get<String>(MESSAGE_BOX_ID)?.toInt()
        senderEmail = savedStateHandle.get<String>(SENDER_EMAIL)
        senderImgUrl = savedStateHandle.get<String>(SENDER_IMG_URL)
        senderUsername = savedStateHandle.get<String>(SENDER_USERNAME)
    }

    fun onEvent(event: CameraUiEvent) {
        when (event) {
            is CameraUiEvent.OnCaptureImageClick -> {
                viewModelScope.launch(ioDispatcher) {
                    CameraController.takePhoto(
                        event.imageCapture,
                        event.context
                    ) { capturedImageUri ->
                        _uiState.update {
                            it.copy(
                                capturedImageUri = capturedImageUri,
                                screenState = if (capturedImageUri == null) ScreenState.CAMERA else ScreenState.CAPTURED_IMAGE
                            )
                        }
                    }
                }
            }

            is CameraUiEvent.OnSendImageClick -> {
                if (senderEmail.isNullOrBlank() || senderUsername.isNullOrBlank()) {
                    SnackbarManager.showMessage(
                        UiText.DynamicString("Something went wrong! Please try again later.")
                    )
                } else {
                    _uiState.update { it.copy(isMessageSending = true) }
                    uploadImageFileUseCase(
                        imageFileName = "$senderEmail${LocalDateTime.now()}",
                        imageFileUri = event.imageUri,
                        onSuccess = {
                            viewModelScope.launch(ioDispatcher) {
                                sendMessageUseCase(
                                    message = Message(
                                        senderEmail = senderEmail!!,
                                        messageContent = it.toString(),
                                        senderImgUrl = senderImgUrl,
                                        senderUsername = senderUsername!!,
                                        messageType = MessageType.IMAGE,
                                        messageBoxId = messageBoxId!!
                                    ),
                                    onFailure = SnackbarManager::showMessage
                                )
                                _uiState.update {
                                    it.copy(
                                        navigateBack = true,
                                        isMessageSending = false
                                    )
                                }
                            }
                        },
                        onFailure = { errorMessage ->
                            SnackbarManager.showMessage(errorMessage)
                            _uiState.update { it.copy(isMessageSending = false) }
                        }
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        CameraController.resetCapturedPhotoUri()
    }
}