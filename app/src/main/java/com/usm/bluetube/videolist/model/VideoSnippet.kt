package com.usm.bluetube.videolist.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoSnippet(
    val title: String,
    val description: String,
    val publishedAt: String,
    val channelTitle: String,
    val channelId: String,
    val thumbnails: Thumbnails
)
