package com.vstudio.pixabay.core.network

import com.vstudio.pixabay.core.network.model.SearchResponse

interface ImagesRemoteDataSource {

    suspend fun searchImages(
        query: String,
        page: Int,
        perPage: Int,
    ): SearchResponse

}