package com.usm.bluetube.videolist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.usm.bluetube.videolist.api.VideoApiService
import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import com.usm.bluetube.videolist.paging.YoutubeVideoSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class VideoListRepositoryImpl @Inject constructor(
    private val apiService: VideoApiService
): VideoListRepository {

    override suspend fun fetchChannels(channelId: String): Response<YoutubeChannelResponse> {
        return apiService.fetchChannels(channelId)
    }

    override fun fetchVideos(viewModelScope: CoroutineScope): Flow<PagingData<YoutubeVideo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 25,
                prefetchDistance = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { YoutubeVideoSource(apiService, viewModelScope) }
        ).flow
    }
}