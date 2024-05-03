package com.ahmetocak.stories

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(StoriesUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _navigationState = MutableStateFlow<NavigationState>(NavigationState.None)
    val navigationState get() = _navigationState.asStateFlow()

    fun onEvent(event: StoriesUiEvent) {
        when (event) {
            is StoriesUiEvent.OnAddStoryClick -> _uiState.update { it.copy(currentScreen = CurrentScreen.AddStory) }
            is StoriesUiEvent.OnStoryClick -> _uiState.update {
                it.copy(currentScreen = CurrentScreen.LookStory(event.id))
            }
        }
    }

    fun resetNavigation() {
        _navigationState.update { NavigationState.None }
    }

    fun resetCurrentScreen() {
        _uiState.update { it.copy(currentScreen = CurrentScreen.Stories) }
    }
}