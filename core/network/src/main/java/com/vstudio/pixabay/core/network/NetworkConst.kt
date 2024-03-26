package com.vstudio.pixabay.core.network

object NetworkConst {
    internal const val BASE_URL = "https://pixabay.com/"
    const val PER_PAGE = 60
    const val MAX_PER_PAGE = 80
    const val DEFAULT_PAGE_INDEX = 1
    const val PAGE_INCREMENT = 1

    const val IMAGES_MEMORY_CACHE_SIZE_PERCENT = 0.50
    const val IMAGES_DISC_CACHE_SIZE_PERCENT = 0.50
}