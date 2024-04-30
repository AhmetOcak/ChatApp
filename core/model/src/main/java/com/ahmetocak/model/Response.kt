package com.ahmetocak.model

sealed interface LoadingState {
    data object Idle : LoadingState
    data object Loading : LoadingState
}