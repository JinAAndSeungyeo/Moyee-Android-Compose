package com.moyee.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moyee.android.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    val isFirstAccess = dataStoreRepository.getFirstAccessStatus()
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    fun updateFirstAccessStatus(isFirstAccess: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.updateFirstAccessStatus(isFirstAccess)
        }
    }
}