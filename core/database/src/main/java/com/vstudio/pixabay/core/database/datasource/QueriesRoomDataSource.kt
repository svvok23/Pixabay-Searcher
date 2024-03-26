package com.vstudio.pixabay.core.database.datasource

import com.vstudio.pixabay.core.database.db.ImagesDatabase
import com.vstudio.pixabay.core.database.QueriesLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class QueriesRoomDataSource @Inject constructor(
    private val imagesDatabase: ImagesDatabase,
): QueriesLocalDataSource {
    override fun getQueries(): Flow<List<String>> = imagesDatabase.remoteKeyDao.getQueriesFlow()
}
