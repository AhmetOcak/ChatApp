package com.ahmetocak.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.ahmetocak.datastore.DataStoreConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): AppPreferencesManager {

    private object PreferenceKeys {
        val APP_THEME = booleanPreferencesKey(DataStoreConstants.APP_THEME_KEY)
        val DYNAMIC_COLOR = booleanPreferencesKey(DataStoreConstants.DYNAMIC_COLOR_KEY)
    }

    override suspend fun observeAppTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.APP_THEME] ?: true
        }
    }

    override suspend fun updateAppTheme(darkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.APP_THEME] = darkMode
        }
    }

    override suspend fun observeDynamicColor(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferenceKeys.DYNAMIC_COLOR] ?: false
        }
    }

    override suspend fun updateDynamicColor(dynamicColor: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.DYNAMIC_COLOR] = dynamicColor
        }
    }
}