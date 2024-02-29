package com.usm.bluetube.videolist.model

data class VideoDetails(
    val title: String,
    val description: String,
    val publishedAt: String,
    val channelTitle: String,
    val thumbnails: Map<String, VideoPreviewUrl>
)
