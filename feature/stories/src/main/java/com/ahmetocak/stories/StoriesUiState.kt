package com.ahmetocak.stories

import com.ahmetocak.model.LoadingState
import com.ahmetocak.model.Story

data class StoriesUiState(
    val currentUserImg: String? = null,
    val storyList: List<Story> = emptyList(),
    val currentScreen: CurrentScreen = CurrentScreen.Stories,
    val loadingState: LoadingState = LoadingState.Loading
)

sealed class StoriesUiEvent {
    data object OnAddStoryClick : StoriesUiEvent()
    data class OnStoryClick(val id: String) : StoriesUiEvent()

}

sealed class NavigationState {
    data class UserDetail(val id: String): NavigationState()
    data object None : NavigationState()
}

sealed class CurrentScreen {
    data object Stories : CurrentScreen()
    data object AddStory : CurrentScreen()
    data class LookStory(val id: String) : CurrentScreen()
}