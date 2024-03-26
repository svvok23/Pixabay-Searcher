package com.vstudio.pixabay.core.domain.usecase

import androidx.paging.PagingData
import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.repository.ImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchImagesUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
) {
    operator fun invoke(query: String): Flow<PagingData<Image>> {
        return imagesRepository.searchImages(query.trim().lowercase())
    }
}