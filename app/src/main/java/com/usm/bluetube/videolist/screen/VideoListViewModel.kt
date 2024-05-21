package com.usm.bluetube.videolist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.usm.bluetube.core.core_util.VideoType
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import com.usm.bluetube.videolist.repository.VideoListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
): ViewModel() {

    fun getVideosFlow(): StateFlow<PagingData<YoutubeVideo>> {
        return videoRepository
            .fetchVideos(VideoType.Videos, viewModelScope).cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    fun getSearchVideosFlow(query: String = ""): StateFlow<PagingData<YoutubeVideo>> {
        return videoRepository
            .fetchVideos(VideoType.SearchedVideo(query), viewModelScope)
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }
}