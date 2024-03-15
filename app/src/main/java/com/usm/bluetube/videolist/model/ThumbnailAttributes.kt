package com.usm.bluetube.videolist.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ThumbnailAttributes(
    val url: String = "",
    val height: Int = 0,
    val width: Int = 0
)