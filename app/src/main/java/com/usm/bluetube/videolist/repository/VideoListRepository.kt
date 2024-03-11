package com.usm.bluetube.videolist.repository

import com.usm.bluetube.videolist.model.YoutubeResponse

interface VideoListRepository {

    suspend fun fetchVideos(): Result<YoutubeResponse>
}