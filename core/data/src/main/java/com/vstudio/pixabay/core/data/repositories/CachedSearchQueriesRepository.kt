package com.vstudio.pixabay.core.data.repositories

import com.vstudio.pixabay.core.database.QueriesLocalDataSource
import com.vstudio.pixabay.core.domain.repository.SearchQueriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CachedSearchQueriesRepository @Inject constructor(
    private val queriesLocalDataSource: QueriesLocalDataSource,
) : SearchQueriesRepository {
    override fun getQueries(): Flow<List<String>> = queriesLocalDataSource.getQueries()
}
