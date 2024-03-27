package com.vstudio.pixabay.core.network

import java.util.concurrent.TimeUnit

object NetworkConst {
    internal const val BASE_URL = "https://pixabay.com/"
    const val PER_PAGE = 60
    const val MAX_PER_PAGE = 80
    const val DEFAULT_PAGE_INDEX = 1
    const val PAGE_INCREMENT = 1

    // Some images URL valid for 24 hours
    val IMAGE_URL_VALID_TIME = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)

    const val IMAGES_MEMORY_CACHE_SIZE_PERCENT = 0.50
    const val IMAGES_DISC_CACHE_SIZE_PERCENT = 0.50
}