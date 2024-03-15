package com.usm.bluetube.videolist.model.videos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoutubeVideo(
    val id: String,
    val snippet: VideoSnippet,
    val contentDetails: ContentDetails,
    val statistics: VideoStatistics
)
