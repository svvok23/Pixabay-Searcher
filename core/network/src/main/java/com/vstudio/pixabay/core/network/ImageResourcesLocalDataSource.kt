package com.vstudio.pixabay.core.network

interface ImageResourcesLocalDataSource {
    fun getCashedImageUrl(imageUrls: List<String?>): String?
}