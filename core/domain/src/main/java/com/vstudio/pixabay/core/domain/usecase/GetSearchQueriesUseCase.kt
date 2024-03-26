package com.vstudio.pixabay.core.domain.usecase

import com.vstudio.pixabay.core.domain.repository.SearchQueriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSearchQueriesUseCase @Inject constructor(
    private val queriesRepository: SearchQueriesRepository,
) {

    operator fun invoke(): Flow<List<String>> {
        return queriesRepository.getQueries().map { it.filter { it.isNotEmpty() }  }
    }

    companion object {
        const val DEFAULT_QUERY = "fruits"
    }
}