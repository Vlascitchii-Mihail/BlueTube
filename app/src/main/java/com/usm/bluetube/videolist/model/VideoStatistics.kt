package com.usm.bluetube.videolist.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoStatistics(
    val viewCount: Long,
    val likeCount: Long,
)