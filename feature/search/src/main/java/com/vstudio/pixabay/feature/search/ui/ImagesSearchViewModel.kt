package com.vstudio.pixabay.feature.search.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.usecase.GetRefreshImagesCauseUseCase
import com.vstudio.pixabay.core.domain.usecase.GetSearchQueriesUseCase
import com.vstudio.pixabay.core.domain.usecase.GetSearchQueriesUseCase.Companion.DEFAULT_QUERY
import com.vstudio.pixabay.core.domain.usecase.SearchImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ImagesSearchViewModel @Inject constructor(
    getRefreshImagesCauseUseCase: GetRefreshImagesCauseUseCase,
    searchImagesUseCase: SearchImagesUseCase,
    getSearchQueriesUseCase: GetSearchQueriesUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val queryToSearch = MutableSharedFlow<String>(replay = 1, extraBufferCapacity = 1)
    val queryFlow: StateFlow<String> = savedStateHandle.getStateFlow(QUERY_KEY, DEFAULT_QUERY)

    private val _queriesHistoryFlow = MutableStateFlow(emptyList<String>())
    val queriesHistoryFlow: StateFlow<List<String>> = _queriesHistoryFlow.asStateFlow()

    private val _toRefresh: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val toRefresh: StateFlow<Boolean> = _toRefresh.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val imagesPagingFlow: Flow<PagingData<Image>> = queryToSearch.flatMapLatest { query ->
        searchImagesUseCase(query)
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
    }

    init {
        onSearch(DEFAULT_QUERY)

        getSearchQueriesUseCase().onEach {
            _queriesHistoryFlow.value = it
        }.launchIn(viewModelScope)

        getRefreshImagesCauseUseCase().onEach {
            _toRefresh.value = it
        }.launchIn(viewModelScope)
    }

    fun onSearch(query: String) {
        queryToSearch.tryEmit(query)
    }

    fun onQueryChanged(query: String) {
        savedStateHandle[QUERY_KEY] = query
    }

    companion object {
        private const val QUERY_KEY = "query"
    }

}