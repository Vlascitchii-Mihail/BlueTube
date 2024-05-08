package com.usm.bluetube.videolist.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.usm.bluetube.core.core_api.VideoApiService
import com.usm.bluetube.core.core_paging.YoutubeVideoSource
import com.usm.bluetube.core.core_util.VideoType
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface VideoListRepository {

    fun fetchVideos(videoType: VideoType, viewModelScope: CoroutineScope): Flow<PagingData<YoutubeVideo>>
    fun fetchSearchVideos(videoType: VideoType, viewModelScope: CoroutineScope): Flow<PagingData<YoutubeVideo>>
}

class VideoListRepositoryImpl @Inject constructor(
    private val apiVideoListService: VideoApiService
): VideoListRepository {

    override fun fetchVideos(videoType: VideoType, viewModelScope: CoroutineScope): Flow<PagingData<YoutubeVideo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 25,
                prefetchDistance = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                YoutubeVideoSource(apiVideoListService, viewModelScope, videoType)
            }
        ).flow
    }

    override fun fetchSearchVideos(videoType: VideoType, viewModelScope: CoroutineScope): Flow<PagingData<YoutubeVideo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                maxSize = 25,
                prefetchDistance = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                YoutubeVideoSource(apiVideoListService, viewModelScope, videoType)
            }
        ).flow
    }
}