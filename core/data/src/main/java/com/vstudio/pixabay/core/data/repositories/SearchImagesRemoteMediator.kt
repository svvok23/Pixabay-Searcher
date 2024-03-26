package com.vstudio.pixabay.core.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.vstudio.pixabay.core.common.Mapper
import com.vstudio.pixabay.core.database.ImagesLocalDataSource
import com.vstudio.pixabay.core.database.model.ImageEntity
import com.vstudio.pixabay.core.database.model.RemoteKey
import com.vstudio.pixabay.core.network.ImagesRemoteDataSource
import com.vstudio.pixabay.core.network.NetworkConst.DEFAULT_PAGE_INDEX
import com.vstudio.pixabay.core.network.NetworkConst.PAGE_INCREMENT
import com.vstudio.pixabay.core.network.model.HitDto
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
internal class SearchImagesRemoteMediator(
    private val query: String = "",
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imageMapper: Mapper<HitDto, ImageEntity>,
) : RemoteMediator<Int, ImageEntity>() {

    override suspend fun initialize(): InitializeAction {
        // Some images URL valid for 24 hours
        val day = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        val updatedTime = imagesLocalDataSource.getOldestCreationTime(query) ?: 0
        return if (System.currentTimeMillis() - updatedTime < day) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ImageEntity>,
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)

                remoteKey?.nextPage?.minus(PAGE_INCREMENT) ?: DEFAULT_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)

                remoteKey?.prevPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }

            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)

                remoteKey?.nextPage
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
        }

        try {
            val imagesResponse = imagesRemoteDataSource.searchImages(
                query = query,
                page = page,
                perPage = state.config.pageSize
            )
            val images = imagesResponse.hits
            val endOfPaginationReached = images.isEmpty() ||
                    page * state.config.pageSize >= imagesResponse.totalHits

            val imageEntities = images.map { imageMapper.mapFrom(it).copy(searchQuery = query) }
            val prevPage = if (page == DEFAULT_PAGE_INDEX) null else page - PAGE_INCREMENT
            val nextPage = if (endOfPaginationReached) null else page + PAGE_INCREMENT
            val remoteKey = RemoteKey(
                id = 0,
                query = query,
                prevPage = prevPage,
                nextPage = nextPage,
                createdAt = System.currentTimeMillis()
            )
            if (loadType == LoadType.REFRESH) {
                imagesLocalDataSource.replaceQueryImagesWithRemoteKey(
                    query,
                    imageEntities,
                    remoteKey
                )
            } else {
                imagesLocalDataSource.addImagesWithRemoteKey(imageEntities, remoteKey)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ImageEntity>,
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.dbId?.let {
                imagesLocalDataSource.getRemoteKey(it)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, ImageEntity>,
    ): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { image ->
            imagesLocalDataSource.getRemoteKey(image.dbId)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, ImageEntity>,
    ): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { image ->
            imagesLocalDataSource.getRemoteKey(image.dbId)
        }
    }
}