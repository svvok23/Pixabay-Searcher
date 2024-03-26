package com.vstudio.pixabay.core.database

import androidx.paging.PagingSource
import com.vstudio.pixabay.core.database.model.ImageEntity
import com.vstudio.pixabay.core.database.model.RemoteKey
import kotlinx.coroutines.flow.Flow

interface ImagesLocalDataSource {

    fun getImages(query: String): PagingSource<Int, ImageEntity>

    fun getImage(imageId: Long): Flow<ImageEntity>

    suspend fun getRemoteKey(id: Long): RemoteKey?

    suspend fun addImagesWithRemoteKey(imageEntities: List<ImageEntity>, remoteKey: RemoteKey)

    suspend fun replaceQueryImagesWithRemoteKey(
        query: String,
        imageEntities: List<ImageEntity>,
        remoteKey: RemoteKey,
    )

    suspend fun getOldestCreationTime(query: String): Long?

}