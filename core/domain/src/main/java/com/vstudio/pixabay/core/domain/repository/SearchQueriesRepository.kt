package com.vstudio.pixabay.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SearchQueriesRepository {
    fun getQueries(): Flow<List<String>>
}