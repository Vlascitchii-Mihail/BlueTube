package com.usm.bluetube.videolist.repository

import androidx.paging.PagingData
import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface VideoListRepository {

    suspend fun fetchChannels(channelId: String): Response<YoutubeChannelResponse>

    fun fetchVideos(viewModelScope: CoroutineScope): Flow<PagingData<YoutubeVideo>>

}