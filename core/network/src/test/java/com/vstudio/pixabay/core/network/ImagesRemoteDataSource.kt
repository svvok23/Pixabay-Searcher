package com.vstudio.pixabay.core.network

import org.junit.Test
import retrofit2.Retrofit
import javax.inject.Inject
import com.vstudio.pixabay.core.network.BuildConfig

class RemoteImagesDataSourceTest {
    @Test
    fun testRetrofitInstance(@Inject retrofit: Retrofit) {
        //Assert that, Retrofit's base url matches to our BASE_URL
        assert(retrofit.baseUrl().url().toString() == BuildConfig.BASE_URL)
    }

/*

    @Test
    fun `when searchImages invoked should invoke api service function with provided query, page index and use default value perPage`() =
        runTest {
            // Given
            val fakeKeyProvider =
                object : ApiKeyProvider {
                    override val apiKey: String = "fakeKey"
                }
            val pixabayService = mockk<PixabayService>(relaxed = true)
            val objectUnderTest =
                RemoteImagesDataSource(
                    pixabayService = pixabayService,
                    apiKeyProvider = fakeKeyProvider,
                )
            // When
            objectUnderTest.searchImages(
                query = "query",
                page = 1,
            )
            // Then
            coVerify {
                pixabayService.searchImages(
                    key = "fakeKey",
                    query = "query",
                    page = 1,
                    perPage = ImagesDataSource.DEFAULT_IMAGES_PER_PAGE,
                )
            }
        }
*/

}