package com.vstudio.pixabay.core.domain.model

import kotlin.math.abs

data class ImageSize(
    val width: Int,
    val height: Int,
) {
    val aspectRatio = width.toFloat() / height.toFloat()
}

data class MultiSizeImage(
    val sizes: Map<ImageSize, String>,
) {
    fun getLargestImageUrl(): String {
        return sizes.maxBy { it.key.width * it.key.height }.value
    }

    fun getNearestUrl(size: ImageSize): String? {
        sizes[size]?.let { return it }
        val sizesList = sizes.keys.toList()
        val nearest = findNearest(size, sizesList)
        return sizes[nearest]
    }

    private fun findNearest(size: ImageSize, sizes: List<ImageSize>): ImageSize? {
        return sizes.minByOrNull { abs(it.width - size.width) + abs(it.height - size.height) }
    }
}

fun getImageSize(aspectRatio: Float, maxSize: Int): ImageSize {
    val isPortrait = aspectRatio < 0
    val width = if (isPortrait) (maxSize * aspectRatio).toInt() else maxSize
    val height = if (isPortrait) maxSize else (maxSize / aspectRatio).toInt()
    return ImageSize(width, height)
}