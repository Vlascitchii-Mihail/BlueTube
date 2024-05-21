package com.usm.bluetube.shortslist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.usm.bluetube.core.core_util.VideoType
import com.usm.bluetube.shortslist.repository.ShortsRepository
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val shortsRepository: ShortsRepository
): ViewModel() {

    fun getShorts(): StateFlow<PagingData<YoutubeVideo>> {
        return shortsRepository.fetchShorts(VideoType.Shorts, viewModelScope)
            .cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }
}