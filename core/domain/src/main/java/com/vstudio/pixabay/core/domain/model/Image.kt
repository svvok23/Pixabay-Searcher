package com.vstudio.pixabay.core.domain.model

data class Image(
    val dbId: Long = 0,
    val id: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    var pageUrl: String = "",
    val previewUrl: String = "",
    val webFormatUrl: String = "",
    val largeImageUrl: String = "",
    val fullHdUrl: String? = null,
    val originalUrl: String? = null,
    val tags: List<String> = emptyList(),
    val user: User = User(),
    val views: Int = 0,
    val downloads: Int = 0,
    val likes: Int = 0,
    val comments: Int = 0,
) {
    val aspectRatio = width.toFloat() / height.toFloat()
    fun getLargestImageUrl(): String? {
        if (originalUrl.isNullOrEmpty().not()) return originalUrl
        if (fullHdUrl.isNullOrEmpty().not()) return fullHdUrl
        if (largeImageUrl.isNotEmpty()) return largeImageUrl
        if (webFormatUrl.isNotEmpty()) return webFormatUrl
        if (previewUrl.isNotEmpty()) return previewUrl
        return null
    }
}


