package com.ahmetocak.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.ahmetocak.datastore.datasource.AppPreferencesManager
import com.ahmetocak.datastore.datasource.AppPreferencesManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatastoreModule {

    @Singleton
    @Provides
    fun provideDatastore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(DataStoreConstants.FILE_NAME)
            }
        )
    }

    @Singleton
    @Provides
    fun provideAppPreferencesManager(dataStore: DataStore<Preferences>): AppPreferencesManager {
        return AppPreferencesManagerImpl(dataStore)
    }
}