package com.vstudio.pixabay.core.network.util

import com.vstudio.pixabay.core.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class PixabayInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val originalUrl = request.url

        val modifiedUrl = originalUrl.newBuilder()
            .addQueryParameter("key", BuildConfig.PIXABAY_API_KEY)
            .build()

        request = request.newBuilder()
            .url(modifiedUrl)
            .build()

        return chain.proceed(request)
    }

}