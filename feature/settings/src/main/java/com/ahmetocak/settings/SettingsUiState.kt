package com.ahmetocak.settings

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val isDynamicColor: Boolean = false,
    val password: String = "",
    val isLoading: Boolean = false,
    val showDeleteAccountDialog: Boolean = false
)

sealed class SettingUiEvent {
    data class OnDarkModeSwitched(val value: Boolean) : SettingUiEvent()
    data class OnDynamicColorSwitched(val value: Boolean) : SettingUiEvent()
    data object OnStartDeleteAccountDialogClick : SettingUiEvent()
    data object OnSubmitDeleteAccountClick : SettingUiEvent()
    data object OnDismissDeleteAccountDialog : SettingUiEvent()
    data object OnSignOutClick : SettingUiEvent()
    data class OnPasswordValueChange(val value: String) : SettingUiEvent()
    data object OnProfileClick : SettingUiEvent()
}

sealed class NavigationState {
    data object None : NavigationState()
    data object Login : NavigationState()
    data object Profile : NavigationState()
    data object Back : NavigationState()
}