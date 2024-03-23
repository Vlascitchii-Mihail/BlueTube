package com.usm.bluetube.videolist.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.usm.bluetube.videolist.api.VideoApiService
import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import com.usm.bluetube.videolist.model.videos.YoutubeVideoResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class YoutubeVideoSource(
    private val apiService: VideoApiService,
    private val coroutineScope: CoroutineScope,
): PagingSource<String, YoutubeVideo>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, YoutubeVideo> {
        return try {
            val nextPageToken = params.key ?: ""
            val videos = getLoadData(nextPageToken)

            LoadResult.Page(
                data = videos?.items ?: emptyList(),
                prevKey = videos?.prevPageToken,
                nextKey = videos?.nextPageToken
            )
        } catch (ex: IOException) {
            return LoadResult.Error(ex)
        } catch (ex: HttpException) {
            return LoadResult.Error(ex)
        }
    }

    private suspend fun getLoadData(nextPageToken: String): YoutubeVideoResponse? {
        val videos = apiService.fetchVideos(nextPageToken = nextPageToken).body()

        videos?.items?.let {
            addChannelUrlToVideo(it,  getChannelsUrl(it))
        }

        return videos
    }

    private suspend fun getChannelsUrl(videos: List<YoutubeVideo>): MutableList<String> {
        val channelUrlList: MutableList<String> = mutableListOf()

        videos.map { video: YoutubeVideo ->
            apiService.fetchChannels(video.snippet.channelId).body()?.let{ channelResponse: YoutubeChannelResponse ->
                channelUrlList.add(channelResponse.items.first().snippet.thumbnails.medium.url)
            }
        }
        return channelUrlList
    }

    private fun addChannelUrlToVideo(
        videos: List<YoutubeVideo>,
        channelUrlList: MutableList<String>
    ) {
        for (i in videos.indices) {
            videos[i].snippet.channelImgUrl = channelUrlList[i]
        }
    }

    override fun getRefreshKey(state: PagingState<String, YoutubeVideo>): String? {
        var current: String? = " "
        val anchorPosition = state.anchorPosition

        coroutineScope.launch {
            if (anchorPosition != null) {
                current = state.closestPageToPosition(anchorPosition)?.prevKey?.let {
                    apiService.fetchVideos(it).body()?.nextPageToken
                }
            }
        }
        return current
    }
}