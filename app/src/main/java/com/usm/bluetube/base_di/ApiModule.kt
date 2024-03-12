package com.usm.bluetube.base_di

import com.usm.bluetube.base_api.Constants.Companion.BASE_URL
import com.usm.bluetube.base_api.InterceptorApiRequest
import com.usm.bluetube.videolist.api.VideoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    private val client = OkHttpClient.Builder().addInterceptor(InterceptorApiRequest()).build()

    @Singleton
    @Provides
    fun provideApi(): VideoApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(VideoApiService::class.java)
}
