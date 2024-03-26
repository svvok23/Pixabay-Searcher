package com.vstudio.pixabay.core.domain.usecase

import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.repository.ImageResourcesRepository
import javax.inject.Inject

class GetCashedImageUrlUseCase @Inject constructor(
    private val imagesRepository: ImageResourcesRepository,
) {
    operator fun invoke(image: Image): String? = imagesRepository.getCashedImageUrl(image)
}