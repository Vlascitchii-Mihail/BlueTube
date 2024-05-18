package com.usm.bluetube.core.core_api

import com.usm.bluetube.core.core_api.Constants.Companion.CHANNELS
import com.usm.bluetube.core.core_api.Constants.Companion.CONTENT_DETAILS
import com.usm.bluetube.core.core_api.Constants.Companion.CONTENT_VIDEO_TYPE
import com.usm.bluetube.core.core_api.Constants.Companion.LIST_OF_VIDEOS
import com.usm.bluetube.core.core_api.Constants.Companion.MOST_POPULAR
import com.usm.bluetube.core.core_api.Constants.Companion.REGION_CODE
import com.usm.bluetube.core.core_api.Constants.Companion.RELEVANCE
import com.usm.bluetube.core.core_api.Constants.Companion.SEARCH
import com.usm.bluetube.core.core_api.Constants.Companion.SHORTS_VIDEO_DURATION
import com.usm.bluetube.core.core_api.Constants.Companion.SINGLE_CHANNEL
import com.usm.bluetube.core.core_api.Constants.Companion.SNIPPET
import com.usm.bluetube.core.core_api.Constants.Companion.STATISTICS
import com.usm.bluetube.search_video.model.SearchVideoResponse
import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.ParticularVideo
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
        @Query("pageToken") nextPageToken: String = ""
    ): Response<YoutubeVideoResponse>

    @GET(CHANNELS)
    suspend fun fetchChannels(
        @Query("id") id: String,
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS",
        @Query("maxResults") maxResults: Int = SINGLE_CHANNEL,
    ): Response<YoutubeChannelResponse>

    @GET(SEARCH)
    suspend fun searchVideo(
        @Query("q") query: String = "",
        @Query("part") part: String = SNIPPET,
        @Query("order") order: String = RELEVANCE,
        @Query("pageToken") nextPageToken: String = ""
    ): Response<SearchVideoResponse>

    @GET(LIST_OF_VIDEOS)
    suspend fun fetchParticularVideo(
        @Query("id") id: String,
        @Query("part") part: String = "$SNIPPET, $CONTENT_DETAILS, $STATISTICS"
    ): Response<ParticularVideo>

    @GET(SEARCH)
    suspend fun fetchShorts(
        @Query("part") part: String = SNIPPET,
        @Query("type") type: String = CONTENT_VIDEO_TYPE,
        @Query("videoDuration") videoDuration: String = SHORTS_VIDEO_DURATION,
        @Query("regionCode") regionCode: String = REGION_CODE,
        @Query("order") order: String = RELEVANCE,
        @Query("pageToken") nextPageToken: String = ""
    ): Response<SearchVideoResponse>
}