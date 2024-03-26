package com.vstudio.pixabay.core.data.repositories

import com.vstudio.pixabay.core.domain.model.Image
import com.vstudio.pixabay.core.domain.repository.ImageResourcesRepository
import com.vstudio.pixabay.core.network.ImageResourcesLocalDataSource
import javax.inject.Inject

internal class CashedImageResourcesRepository @Inject constructor(
    private val imageResources: ImageResourcesLocalDataSource,
) : ImageResourcesRepository {

    override fun getCashedImageUrl(image: Image): String? {
        val urls = with(image) { listOf(originalUrl, largeImageUrl, webFormatUrl, previewUrl) }
        return imageResources.getCashedImageUrl(urls)
    }
}