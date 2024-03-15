package com.usm.bluetube.videolist.api

import com.usm.bluetube.base_api.Constants.Companion.CHANNELS
import com.usm.bluetube.base_api.Constants.Companion.CONTENT_DETAILS
import com.usm.bluetube.base_api.Constants.Companion.LIST_OF_VIDEOS
import com.usm.bluetube.base_api.Constants.Companion.MOST_POPULAR
import com.usm.bluetube.base_api.Constants.Companion.REGION_CODE
import com.usm.bluetube.base_api.Constants.Companion.SINGLE_CHANNEL
import com.usm.bluetube.base_api.Constants.Companion.SNIPPET
import com.usm.bluetube.base_api.Constants.Companion.STATISTICS
import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApiService {

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchVideos(
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
        @Query("chart") chart: String = MOST_POPULAR,
        @Query("regionCode") regionCode: String = REGION_CODE,
    ): Response<YoutubeVideoResponse>

    @GET(CHANNELS)
    suspend fun fetchChannels(
        @Query("id") id: String,
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
        @Query("maxResults") maxResults: Int = SINGLE_CHANNEL,
    ): Response<YoutubeChannelResponse>
}