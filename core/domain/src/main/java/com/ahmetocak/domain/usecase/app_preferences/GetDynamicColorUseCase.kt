package com.ahmetocak.domain.usecase.app_preferences

import com.ahmetocak.datastore.datasource.AppPreferencesManager
import javax.inject.Inject

class GetDynamicColorUseCase @Inject constructor(private val appPreferencesManager: AppPreferencesManager) {

    suspend operator fun invoke() = appPreferencesManager.observeDynamicColor()
}