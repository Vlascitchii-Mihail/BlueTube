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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val videoRepository: VideoListRepository
): ViewModel() {

    private var _youtubeResponse: MutableStateFlow<YoutubeResponse> = MutableStateFlow(YoutubeResponse())
    val youtubeResponse: StateFlow<YoutubeResponse> = _youtubeResponse

    private var _errorMsgResponse: MutableSharedFlow<String> = MutableSharedFlow()
    val errorMsgResponse: SharedFlow<String> = _errorMsgResponse

    fun fetchVideos() {
        viewModelScope.launch {
            try {
                val response = videoRepository.fetchVideos()
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    _youtubeResponse.value = body
                } else {
                    _errorMsgResponse.emit("Internal error. Try later.")
                }
            } catch (ex: Exception) {
                _errorMsgResponse.emit("Check your internet Connection")
                print("Check your internet Connection")
            }
        }
    }
}