package com.usm.bluetube.videolist.model.videos

import com.squareup.moshi.JsonClass
import com.usm.bluetube.videolist.model.Thumbnails

@JsonClass(generateAdapter = true)
data class VideoSnippet(
    val title: String,
    val description: String,
    val publishedAt: String,
    val channelTitle: String,
    var channelImgUrl: String = "",
    val channelId: String,
    val thumbnails: Thumbnails
)
