package com.vstudio.pixabay.core.domain.repository

import androidx.paging.PagingData
import com.vstudio.pixabay.core.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface ImagesRepository {
    fun searchImages(searchString: String): Flow<PagingData<Image>>
    fun getImage(imageId: Long): Flow<Image>
}