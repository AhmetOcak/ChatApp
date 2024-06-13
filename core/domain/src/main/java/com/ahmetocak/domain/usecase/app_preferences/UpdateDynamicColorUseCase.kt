package com.ahmetocak.domain.usecase.app_preferences

import com.ahmetocak.datastore.datasource.AppPreferencesManager
import javax.inject.Inject

class UpdateDynamicColorUseCase @Inject constructor(private val appPreferencesManager: AppPreferencesManager) {

    suspend operator fun invoke(dynamicColor: Boolean) =
        appPreferencesManager.updateDynamicColor(dynamicColor)
}