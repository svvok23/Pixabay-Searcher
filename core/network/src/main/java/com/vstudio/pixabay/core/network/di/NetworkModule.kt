package com.vstudio.pixabay.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.vstudio.pixabay.core.network.BuildConfig
import com.vstudio.pixabay.core.network.ImagesRemoteDataSource
import com.vstudio.pixabay.core.network.datasource.ImagesRetrofitDataSource
import com.vstudio.pixabay.core.network.NetworkConst
import com.vstudio.pixabay.core.network.api.PixabayAPI
import com.vstudio.pixabay.core.network.util.EnumConverterFactory
import com.vstudio.pixabay.core.network.util.PixabayInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @Binds
    abstract fun bindsImagesRemoteDataSource(
        imagesRemoteDataSource: ImagesRetrofitDataSource,
    ): ImagesRemoteDataSource

    companion object {
        @Singleton
        @Provides
        internal fun providePixabayApi(retrofit: Retrofit): PixabayAPI {
            return retrofit.create(PixabayAPI::class.java)
        }

        @Singleton
        @Provides
        fun provideRetrofit(
            okHttpClient: OkHttpClient,
            converterFactory: Converter.Factory,
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl(NetworkConst.BASE_URL)
                .addConverterFactory(converterFactory)
                .addConverterFactory(EnumConverterFactory())
                .client(okHttpClient)
                .build()
        }

        @Singleton
        @Provides
        fun provideKotlinSerialization(): Converter.Factory {
            val contentType = "application/json".toMediaType()
            val json = Json {
                ignoreUnknownKeys = true
            }
            return json.asConverterFactory(contentType)
        }

        @Singleton
        @Provides
        fun provideOkHttpClient(pixabayInterceptor: PixabayInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(pixabayInterceptor)
                .apply {
                    if (BuildConfig.DEBUG) {
                        val interceptor = HttpLoggingInterceptor().apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                        addInterceptor(interceptor)
                    }
                }
                .build()
        }
    }
}
