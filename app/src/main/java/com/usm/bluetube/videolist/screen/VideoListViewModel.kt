package com.usm.bluetube.videolist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import com.usm.bluetube.videolist.repository.VideoListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoListRepository,
): ViewModel() {

    val videosFlow: Flow<PagingData<YoutubeVideo>> = videoRepository
        .fetchVideos(viewModelScope).cachedIn(viewModelScope)
}