package com.vstudio.pixabay.core.network.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.vstudio.pixabay.core.network.BuildConfig
import com.vstudio.pixabay.core.network.datasource.ImageResourcesCoilDataSource
import com.vstudio.pixabay.core.network.ImageResourcesLocalDataSource
import com.vstudio.pixabay.core.network.NetworkConst.IMAGES_DISC_CACHE_SIZE_PERCENT
import com.vstudio.pixabay.core.network.NetworkConst.IMAGES_MEMORY_CACHE_SIZE_PERCENT
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ImagesModule {

    @Binds
    abstract fun bindsImageResourcesLocalDataSource(
        imageResources: ImageResourcesCoilDataSource,
    ): ImageResourcesLocalDataSource

    companion object {

        @Provides
        @Singleton
        fun provideImageLoader(
            @ApplicationContext application: Context,
        ): ImageLoader {
            return ImageLoader.Builder(application)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .memoryCache {
                    MemoryCache.Builder(application)
                        .maxSizePercent(IMAGES_MEMORY_CACHE_SIZE_PERCENT)
                        .strongReferencesEnabled(true)
                        .build()
                }
                .diskCachePolicy(CachePolicy.ENABLED)
                .diskCache {
                    DiskCache.Builder()
                        .directory(application.cacheDir.resolve("image_cache"))
                        .maxSizePercent(IMAGES_DISC_CACHE_SIZE_PERCENT)
                        .build()
                }
                // Assume most content images are versioned urls
                // but some problematic images are fetching each time
                .respectCacheHeaders(false)
                .apply {
                    if (BuildConfig.DEBUG) {
                        logger(DebugLogger())
                    }
                }
                .build()
        }

    }
}