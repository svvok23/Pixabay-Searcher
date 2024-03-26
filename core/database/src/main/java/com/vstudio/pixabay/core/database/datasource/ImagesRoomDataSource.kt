package com.vstudio.pixabay.core.database.datasource

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.vstudio.pixabay.core.database.db.ImagesDatabase
import com.vstudio.pixabay.core.database.ImagesLocalDataSource
import com.vstudio.pixabay.core.database.model.ImageEntity
import com.vstudio.pixabay.core.database.model.RemoteKey
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ImagesRoomDataSource @Inject constructor(
    private val imagesDb: ImagesDatabase,
) : ImagesLocalDataSource {

    override fun getImages(query: String): PagingSource<Int, ImageEntity> {
        return imagesDb.imageDao.getImages(query)
    }

    override fun getImage(imageId: Long): Flow<ImageEntity> {
        return imagesDb.imageDao.getImage(imageId)
    }

    override suspend fun getRemoteKey(id: Long): RemoteKey? {
        return imagesDb.remoteKeyDao.getKey(id)
    }

    override suspend fun addImagesWithRemoteKey(
        imageEntities: List<ImageEntity>,
        remoteKey: RemoteKey,
    ) {
        imagesDb.withTransaction {
            val imagesIds = imagesDb.imageDao.insertAll(imageEntities)
            val keys = imagesIds.map { remoteKey.copy(id = it) }
            imagesDb.remoteKeyDao.insertAll(keys)
        }
    }

    override suspend fun replaceQueryImagesWithRemoteKey(
        query: String,
        imageEntities: List<ImageEntity>,
        remoteKey: RemoteKey,
    ) {
        imagesDb.withTransaction {
            imagesDb.remoteKeyDao.clearAll(query)
            imagesDb.imageDao.clearAll(query)

            val imagesIds = imagesDb.imageDao.insertAll(imageEntities)
            val keys = imagesIds.map { remoteKey.copy(id = it) }
            imagesDb.remoteKeyDao.insertAll(keys)
        }
    }

    override suspend fun getOldestCreationTime(query: String): Long? {
        return imagesDb.remoteKeyDao.getOldestCreationTime(query) ?: 0
    }

}