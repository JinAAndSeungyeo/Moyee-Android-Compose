package com.moyee.android.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.moyee.android.di.FIRST_ACCESS_STATUS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : DataStoreRepository {

    override suspend fun updateFirstAccessStatus(isFirstAccess: Boolean) {
        dataStore.edit { settings ->
            settings[FIRST_ACCESS_STATUS] = isFirstAccess
        }
    }

    override fun getFirstAccessStatus(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FIRST_ACCESS_STATUS] ?: true
    }
}