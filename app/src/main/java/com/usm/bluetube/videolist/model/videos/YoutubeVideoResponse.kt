package com.usm.bluetube.videolist.model.videos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoutubeVideoResponse(
    val nextPageToken: String? = null,
    val prevPageToken: String? = null,
    val items: List<YoutubeVideo> = emptyList()
)