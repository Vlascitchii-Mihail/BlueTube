package com.usm.bluetube.videolist.repository

import com.usm.bluetube.videolist.api.VideoApiService
import com.usm.bluetube.videolist.model.YoutubeResponse
import retrofit2.Response
import javax.inject.Inject

class VideoListRepositoryImpl @Inject constructor(
    private val apiService: VideoApiService
): VideoListRepository {

    override suspend fun fetchVideos(): Response<YoutubeResponse> = apiService.fetchVideos()
}