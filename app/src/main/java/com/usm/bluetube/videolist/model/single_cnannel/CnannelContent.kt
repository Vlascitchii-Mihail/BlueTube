package com.usm.bluetube.videolist.model.single_cnannel

import com.squareup.moshi.JsonClass
import com.usm.bluetube.videolist.model.Thumbnails

@JsonClass(generateAdapter = true)
data class ChannelContent(
    val title: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val thumbnails: Thumbnails = Thumbnails()
)
