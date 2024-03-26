package com.vstudio.pixabay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vstudio.pixabay.core.domain.usecase.GetNetworkStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    getNetworkStatusUseCase: GetNetworkStatusUseCase,
) : ViewModel() {

    val isOffline: StateFlow<Boolean> = getNetworkStatusUseCase()
        .map(Boolean::not)
        .stateIn(
            scope = viewModelScope,
            initialValue = true,
            started = SharingStarted.WhileSubscribed(1_000),
        )
}