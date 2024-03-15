package com.usm.bluetube.videolist.repository

import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideoResponse
import retrofit2.Response

interface VideoListRepository {

    suspend fun fetchVideos(): Response<YoutubeVideoResponse>
    suspend fun fetchChannels(channelId: String): Response<YoutubeChannelResponse>
}