package com.vstudio.pixabay.core.domain.usecase

import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.repository.ImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetImageUseCase @Inject constructor(
    private val imagesRepository: ImagesRepository,
) {
    operator fun invoke(imageId: Long): Flow<Image> {
        return imagesRepository.getImage(imageId)
    }
}