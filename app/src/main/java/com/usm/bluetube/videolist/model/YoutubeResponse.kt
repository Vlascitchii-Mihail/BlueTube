package com.usm.bluetube.videolist.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoutubeResponse(
    val nextPageToken: String = "",
    val items: List<YoutubeVideo> = emptyList()
)