package com.usm.bluetube.videolist.screen

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usm.bluetube.base_api.Constants.Companion.SAVE_NUMBER_OF_CHANNELS
import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideoResponse
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

    private var _youtubeVideoResponse: MutableStateFlow<YoutubeVideoResponse> = MutableStateFlow(
        YoutubeVideoResponse()
    )
    val youtubeVideoResponse: StateFlow<YoutubeVideoResponse> = _youtubeVideoResponse

    private var _errorMsgResponse: MutableSharedFlow<String> = MutableSharedFlow()
    val errorMsgResponse: SharedFlow<String> = _errorMsgResponse

    private val _channelResponse: MutableSharedFlow<Map<ImageView, YoutubeChannelResponse>> =
        MutableSharedFlow(replay = SAVE_NUMBER_OF_CHANNELS)
    val channelResponse: SharedFlow<Map<ImageView, YoutubeChannelResponse>> = _channelResponse

    fun fetchVideos() {
        viewModelScope.launch {
            try {
                val response = videoRepository.fetchVideos()
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    _youtubeVideoResponse.value = body
                } else {
                    _errorMsgResponse.emit("Internal error. Try later.")
                }
            } catch (ex: Exception) {
                _errorMsgResponse.emit("Check your internet Connection")
            }
        }
    }

    fun fetchChannelImage(channelId: String, channelImageView: ImageView) {
        viewModelScope.launch {
            try {
                val response = videoRepository.fetchChannels(channelId)
                val body = response.body()
                Log.d("TAG", "fetchChannelImage() called$body")
                if (response.isSuccessful && body != null) {
                    _channelResponse.emit(mapOf(channelImageView to body))
                } else {
                    _errorMsgResponse.emit("Internal error. Try later.")
                }
            } catch (ex: Exception) {
                _errorMsgResponse.emit("Check your internet Connection")
                ex.printStackTrace()
            }
        }
    }
}