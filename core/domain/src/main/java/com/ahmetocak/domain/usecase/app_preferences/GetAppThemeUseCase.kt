package com.ahmetocak.domain.usecase.app_preferences

import com.ahmetocak.datastore.datasource.AppPreferencesManager
import javax.inject.Inject

class GetAppThemeUseCase @Inject constructor(private val appPreferencesManager: AppPreferencesManager) {

    suspend operator fun invoke() = appPreferencesManager.observeAppTheme()
}