package com.vstudio.pixabay.core.database

import kotlinx.coroutines.flow.Flow

interface QueriesLocalDataSource {
    fun getQueries(): Flow<List<String>>
}