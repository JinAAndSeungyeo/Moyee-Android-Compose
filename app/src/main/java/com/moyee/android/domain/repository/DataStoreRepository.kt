package com.moyee.android.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun updateFirstAccessStatus(isFirstAccess: Boolean)

    fun getFirstAccessStatus(): Flow<Boolean>
}