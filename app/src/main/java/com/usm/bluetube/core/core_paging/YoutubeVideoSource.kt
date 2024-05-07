package com.usm.bluetube.core.core_paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.usm.bluetube.core.core_api.VideoApiService
import com.usm.bluetube.core.core_util.VideoType
import com.usm.bluetube.search_video.model.SearchVideoItem
import com.usm.bluetube.search_video.model.SearchVideoResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import com.usm.bluetube.videolist.model.videos.YoutubeVideoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class YoutubeVideoSource(
    private val apiService: VideoApiService,
    private val viewModelScope: CoroutineScope,
    private val videoType: VideoType
): PagingSource<String, YoutubeVideo>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, YoutubeVideo> {
        return try {
            val nextPageToken = params.key ?: ""
            val videos = getLoadData(nextPageToken, videoType)

            Log.d("YoutubeVideoSource", "load() video result = ${videos?.items?.size}")
            LoadResult.Page(
                data = videos?.items ?: emptyList(),
                prevKey = videos?.prevPageToken,
                nextKey = videos?.nextPageToken
            )
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            return LoadResult.Error(ex)
        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }

    private suspend fun getLoadData(nextPageToken: String, videoType: VideoType): YoutubeVideoResponse? {
        return when(videoType) {
            is VideoType.Videos -> fetchVideos(nextPageToken)
            is VideoType.SearchedVideo -> fetchSearchedVideos(videoType.query, nextPageToken)
        }
    }

    private suspend fun fetchVideos(nextPageToken: String): YoutubeVideoResponse {

        //TODO: try to use runCatching
//        val videos1 = Result.runCatching { apiService.fetchVideos(nextPageToken = nextPageToken) }
        val videos = apiService.fetchVideos(nextPageToken = nextPageToken).body()!!
        videos.items.addChannelUrl()
        Log.d("fetchVideos", "fetchVideos() called with: nextPageToken = ${videos.items.size}")
        return videos
    }

    private suspend fun fetchSearchedVideos(query: String, nextPageToken: String): YoutubeVideoResponse {
        val searchedVideos = apiService.searchVideo(query, nextPageToken = nextPageToken).body()!!
        val newSearchedVideos = searchedVideos.deleteFirstSameVideo()
        Log.d("YoutubeVideoSource", "fetchSearchedVideos() called with: searchedVideo = ${searchedVideos.items.size}")
        val videos: List<YoutubeVideo> = newSearchedVideos.items.convertToVideosList()
        videos.addChannelUrl()
        return YoutubeVideoResponse(newSearchedVideos.nextPageToken, newSearchedVideos.prevPageToken, items = videos)
    }

    private fun SearchVideoResponse.deleteFirstSameVideo(): SearchVideoResponse {
        val mutableVideoList = this.items.toMutableList()
        mutableVideoList.removeAt(0)
        Log.d("YoutubeVideoSource", "deleteFirstSameVideo() called with: searchedVideo = ${this.items.size}")
        return this.copy(items = mutableVideoList)
    }

    private suspend fun  List<SearchVideoItem>.convertToVideosList(): List<YoutubeVideo> {
        val videoList: MutableList<Deferred<YoutubeVideo>> = mutableListOf()
        coroutineScope {
            this@convertToVideosList.forEach { searchedVideo ->
                Log.d("YoutubeVideoSource", "convertToVideosList() called with: searchedVideo = ${searchedVideo.id.videoId}")
                val video = async { searchedVideo.convertToVideo() }
                videoList.add(video)
            }
        }
        return videoList.awaitAll()
    }

    private suspend fun  List<YoutubeVideo>?.addChannelUrl() {
        this?.let {
            addUrl(it,  getChannelsUrl(it))
        }
    }

    private fun addUrl(
        videos: List<YoutubeVideo>,
        channelUrlList: List<String>
    ) {
        for (i in videos.indices) {
            videos[i].snippet.channelImgUrl = channelUrlList[i]
        }
    }

    private suspend fun SearchVideoItem.convertToVideo(): YoutubeVideo {
        val video = apiService.fetchParticularVideo(this.id.videoId).body()!!
        Log.d("YoutubeVideoSource", "convertToVideo() called ${this.id.videoId}")
        return video.items.first()
    }

    //TODO: use async/await pattern
    private suspend fun getChannelsUrl(videos: List<YoutubeVideo>): List<String> {
        val channelUrlList: MutableList<Deferred<String>> = mutableListOf()
        coroutineScope {
            videos.map { video: YoutubeVideo ->
                val channelResponse = apiService.fetchChannels(video.snippet.channelId).body()!!
                val channelIrl = async { channelResponse.items.first().snippet.thumbnails.medium.url }
                channelUrlList.add(channelIrl)
            }
        }
        return channelUrlList.awaitAll()
    }

    override fun getRefreshKey(state: PagingState<String, YoutubeVideo>): String? {
        var current: String? = " "
        val anchorPosition = state.anchorPosition

        viewModelScope.launch {
            if (anchorPosition != null) {
                current = state.closestPageToPosition(anchorPosition)?.prevKey?.let {
                    apiService.fetchVideos(nextPageToken = it).body()?.nextPageToken
                }
            }
        }
        return current
    }
}