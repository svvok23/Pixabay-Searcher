package com.vstudio.pixabay.core.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.vstudio.pixabay.core.common.Mapper
import com.vstudio.pixabay.core.database.ImagesLocalDataSource
import com.vstudio.pixabay.core.database.model.ImageEntity
import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.repository.ImagesRepository
import com.vstudio.pixabay.core.network.ImagesRemoteDataSource
import com.vstudio.pixabay.core.network.NetworkConst.PER_PAGE
import com.vstudio.pixabay.core.network.model.HitDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class OfflineFirstImagesRepository @Inject constructor(
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
    private val imagesLocalDataSource: ImagesLocalDataSource,
    private val imageDtoToEntityMapper: Mapper<HitDto, ImageEntity>,
    private val imageEntityToModelMapper: Mapper<ImageEntity, Image>,
) : ImagesRepository {

    @ExperimentalPagingApi
    override fun searchImages(searchString: String): Flow<PagingData<Image>> {

        return Pager(
            config = PagingConfig(
                pageSize = PER_PAGE,
                prefetchDistance = PER_PAGE / 2,
                initialLoadSize = (PER_PAGE * 0.9).toInt(),
                enablePlaceholders = true,
            ),
            remoteMediator = SearchImagesRemoteMediator(
                query = searchString,
                imagesRemoteDataSource = imagesRemoteDataSource,
                imagesLocalDataSource = imagesLocalDataSource,
                imageMapper = imageDtoToEntityMapper
            ),
            pagingSourceFactory = {
                imagesLocalDataSource.getImages(searchString)
            }
        ).flow.map { pagingData ->
            pagingData.map { imageEntityToModelMapper.mapFrom(it) }
        }
    }

    override fun getImage(imageId: Long): Flow<Image> {
        return imagesLocalDataSource.getImage(imageId).map {
            imageEntityToModelMapper.mapFrom(it)
        }
    }
}