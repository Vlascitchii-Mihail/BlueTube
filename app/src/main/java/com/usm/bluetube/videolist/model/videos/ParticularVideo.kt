package com.usm.bluetube.videolist.model.videos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ParticularVideo(
    val items: List<YoutubeVideo> = emptyList()
)
