package com.vstudio.pixabay.core.network.api

import androidx.annotation.IntRange
import com.vstudio.pixabay.core.network.NetworkConst.DEFAULT_PAGE_INDEX
import com.vstudio.pixabay.core.network.NetworkConst.MAX_PER_PAGE
import com.vstudio.pixabay.core.network.NetworkConst.PER_PAGE
import com.vstudio.pixabay.core.network.model.ImageType
import com.vstudio.pixabay.core.network.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service to fetch images posts using Pixabay API (https://pixabay.com/api/docs/#api_search_images)
 */
internal interface PixabayAPI {

    @GET("api/")
    suspend fun searchImages(
        @Query("q")
        query: String = "",
        @Query("image_type")
        imageType: ImageType = ImageType.ALL,
        @Query("page")
        @IntRange(from = DEFAULT_PAGE_INDEX.toLong())
        page: Int = DEFAULT_PAGE_INDEX,
        @Query("per_page")
        @IntRange(from = DEFAULT_PAGE_INDEX.toLong(), to = MAX_PER_PAGE.toLong())
        perPage: Int = PER_PAGE,
    ): SearchResponse

}