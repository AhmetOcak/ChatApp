package com.ahmetocak.domain.usecase.app_preferences

import com.ahmetocak.datastore.datasource.AppPreferencesManager
import javax.inject.Inject

class UpdateDarkModeUseCase @Inject constructor(private val appPreferencesManager: AppPreferencesManager) {

    suspend operator fun invoke(darkMode: Boolean) = appPreferencesManager.updateAppTheme(darkMode)
}