package com.ahmetocak.calls

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CallsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CallsUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState = _navigationState.asStateFlow()

    private val callsImageState get() = _uiState.value.callImgState

    fun onEvent(event: CallsUiEvent) {
        when (event) {
            is CallsUiEvent.OnCallClick -> _navigationState.update {
                NavigationState.CallDetail(event.id)
            }

            is CallsUiEvent.OnCallImageClick -> {
                when (callsImageState) {
                    is CallsImageClickState.Idle -> _uiState.update {
                        it.copy(callImgState = CallsImageClickState.ShowMiniChatMenu)
                    }

                    is CallsImageClickState.ShowMiniChatMenu -> _uiState.update {
                        it.copy(callImgState = CallsImageClickState.ShowFullScreenImage)
                    }

                    is CallsImageClickState.ShowFullScreenImage -> Unit
                }
            }

            is CallsUiEvent.OnLoadingStateChanged -> _uiState.update {
                it.copy(loadingState = event.state)
            }

            is CallsUiEvent.OnCallLongClick -> _uiState.update { it.copy(showCallListToolbar = true) }
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }

    fun resetCallListToolbar() {
        _uiState.update {
            it.copy(showCallListToolbar = false)
        }
    }
}