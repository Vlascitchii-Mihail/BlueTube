package com.usm.bluetube.videolist.api

import com.usm.bluetube.base_api.Constants.Companion.LIST_OF_VIDEOS
import com.usm.bluetube.base_api.Constants.Companion.MOST_POPULAR
import com.usm.bluetube.base_api.Constants.Companion.REGION_CODE
import com.usm.bluetube.base_api.Constants.Companion.VIDEO_DETAILS
import com.usm.bluetube.base_api.Constants.Companion.VIDEO_SNIPPED
import com.usm.bluetube.base_api.Constants.Companion.VIDEO_STATISTICS
import com.usm.bluetube.videolist.model.YoutubeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApiService {

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchVideos(
        @Query("part") part: String = "$VIDEO_SNIPPED, $VIDEO_DETAILS, $VIDEO_STATISTICS",
        @Query("chart") chart: String = MOST_POPULAR,
        @Query("regionCode") regionCode: String = REGION_CODE,
    ): Response<YoutubeResponse>
}