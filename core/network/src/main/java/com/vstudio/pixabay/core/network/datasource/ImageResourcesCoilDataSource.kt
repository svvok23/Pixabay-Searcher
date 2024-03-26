package com.vstudio.pixabay.core.network.datasource

import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.memory.MemoryCache
import com.vstudio.pixabay.core.network.ImageResourcesLocalDataSource
import javax.inject.Inject

internal class ImageResourcesCoilDataSource @Inject constructor(
    imageLoader: ImageLoader,
) : ImageResourcesLocalDataSource {
    private val memoryCache = imageLoader.memoryCache
    private val diskCache = imageLoader.diskCache

    @OptIn(ExperimentalCoilApi::class)
    override fun getCashedImageUrl(imageUrls: List<String?>): String? {

        val keys = imageUrls.filterNotNull().filter { it.isNotEmpty() }.map { MemoryCache.Key(it) }

        keys.forEach { cashKey ->
            memoryCache?.get(cashKey)?.let { return cashKey.key }
            diskCache?.openSnapshot(cashKey.key)?.let { return cashKey.key }
        }

        return null
    }
}