package com.vstudio.pixabay.core.data.di

import com.vstudio.pixabay.core.common.Mapper
import com.vstudio.pixabay.core.data.mapper.ImageMapperDtoToEntity
import com.vstudio.pixabay.core.data.mapper.ImageMapperDtoToModel
import com.vstudio.pixabay.core.data.mapper.ImageMapperEntityToModel
import com.vstudio.pixabay.core.data.repositories.CachedSearchQueriesRepository
import com.vstudio.pixabay.core.data.repositories.CashedImageResourcesRepository
import com.vstudio.pixabay.core.data.repositories.OfflineFirstImagesRepository
import com.vstudio.pixabay.core.data.util.ConnectivityManagerNetworkMonitor
import com.vstudio.pixabay.core.database.model.ImageEntity
import com.vstudio.pixabay.core.domain.repository.ImagesRepository
import com.vstudio.pixabay.core.domain.repository.NetworkMonitor
import com.vstudio.pixabay.core.domain.repository.SearchQueriesRepository
import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.repository.ImageResourcesRepository
import com.vstudio.pixabay.core.network.model.HitDto
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsNetworkMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor,
    ): NetworkMonitor

    @Binds
    abstract fun bindsImagesRepository(
        imagesRepository: OfflineFirstImagesRepository,
    ): ImagesRepository

    @Binds
    abstract fun bindsSearchQueriesRepository(
        queriesRepository: CachedSearchQueriesRepository
    ): SearchQueriesRepository

    @Binds
    abstract fun bindsImageResourcesRepository(
        imageResourcesRepository: CashedImageResourcesRepository
    ): ImageResourcesRepository

    @Binds
    abstract fun bindsImageMapperDtoToModel(
        imageMapper: ImageMapperDtoToModel,
    ): Mapper<HitDto, Image>

    @Binds
    abstract fun bindsImageMapperDtoToEntity(
        imageMapper: ImageMapperDtoToEntity,
    ): Mapper<HitDto, ImageEntity>

    @Binds
    abstract fun bindsImageMapperEntityToModel(
        imageMapper: ImageMapperEntityToModel,
    ): Mapper<ImageEntity, Image>
}