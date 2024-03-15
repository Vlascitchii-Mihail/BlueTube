package com.usm.bluetube.videolist.repository

import com.usm.bluetube.videolist.api.VideoApiService
import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideoResponse
import retrofit2.Response
import javax.inject.Inject

class VideoListRepositoryImpl @Inject constructor(
    private val apiService: VideoApiService
): VideoListRepository {

    override suspend fun fetchVideos(): Response<YoutubeVideoResponse> = apiService.fetchVideos()
    override suspend fun fetchChannels(channelId: String): Response<YoutubeChannelResponse> {
        return apiService.fetchChannels(channelId)
    }
}