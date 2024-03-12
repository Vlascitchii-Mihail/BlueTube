package com.usm.bluetube.videolist.repository

import com.usm.bluetube.videolist.model.YoutubeResponse
import retrofit2.Response

interface VideoListRepository {

    suspend fun fetchVideos(): Response<YoutubeResponse>
}