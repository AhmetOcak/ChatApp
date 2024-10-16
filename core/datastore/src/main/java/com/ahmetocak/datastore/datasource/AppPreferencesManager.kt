package com.ahmetocak.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface AppPreferencesManager {
    suspend fun observeAppTheme(): Flow<Boolean>

    suspend fun updateAppTheme(darkMode: Boolean)

    suspend fun observeDynamicColor(): Flow<Boolean>

    suspend fun updateDynamicColor(dynamicColor: Boolean)
}