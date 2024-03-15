package com.usm.bluetube.videolist.model.single_cnannel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class YoutubeChannelResponse(
    val items: List<Channel> = emptyList()
)
