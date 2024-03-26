package com.vstudio.pixabay.core.network.datasource

import com.vstudio.pixabay.core.network.ImagesRemoteDataSource
import com.vstudio.pixabay.core.network.api.PixabayAPI
import com.vstudio.pixabay.core.network.model.ImageType
import javax.inject.Inject

internal class ImagesRetrofitDataSource @Inject constructor(
    private val pixabayApi: PixabayAPI,
): ImagesRemoteDataSource {

    override suspend fun searchImages(
        query: String,
        page: Int,
        perPage: Int,
    ) = pixabayApi.searchImages(
        query = query,
        imageType = ImageType.PHOTO,
        page = page,
        perPage = perPage
    )

}