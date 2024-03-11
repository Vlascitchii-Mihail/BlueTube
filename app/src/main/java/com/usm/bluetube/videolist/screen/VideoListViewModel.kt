package com.usm.bluetube.videolist.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usm.bluetube.videolist.model.YoutubeResponse
import com.usm.bluetube.videolist.repository.VideoListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoListRepository
): ViewModel() {

    private var _youtubeResponse: MutableStateFlow<YoutubeResponse> = MutableStateFlow(YoutubeResponse())
    val youtubeResponse: StateFlow<YoutubeResponse> = _youtubeResponse

    private var _errorMsgResponse: MutableSharedFlow<String> = MutableSharedFlow()
    val errorMsgResponse: SharedFlow<String> = _errorMsgResponse.asSharedFlow()

    fun fetchVideos() {
        try {
            viewModelScope.launch {
                videoRepository.fetchVideos()
                    .onSuccess { _youtubeResponse.value = it }
                    .onFailure { it.message?.let { errorMsg -> _errorMsgResponse.emit(errorMsg) } }
            }
        } catch (ex: IOException) {
            ex.message?.let { errorMsg -> _errorMsgResponse.tryEmit(errorMsg) }
        }
    }
}