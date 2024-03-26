package com.vstudio.pixabay.core.domain.repository

import com.vstudio.pixabay.core.domain.model.Image

interface ImageResourcesRepository {
    fun getCashedImageUrl(image: Image): String?
}