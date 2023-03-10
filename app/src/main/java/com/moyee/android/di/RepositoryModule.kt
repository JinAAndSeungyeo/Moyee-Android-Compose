package com.moyee.android.di

import com.moyee.android.domain.repository.DataStoreRepository
import com.moyee.android.domain.repository.DataStoreRepositoryImpl
import com.moyee.android.domain.repository.ValidationRepository
import com.moyee.android.domain.repository.ValidationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindsDataStoreRepository(dataStoreRepositoryImpl: DataStoreRepositoryImpl): DataStoreRepository

    @Binds
    fun bindsValidationRepository(validationRepositoryImpl: ValidationRepositoryImpl): ValidationRepository
}