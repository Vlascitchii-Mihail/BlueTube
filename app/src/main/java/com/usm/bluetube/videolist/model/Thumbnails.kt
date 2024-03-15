package com.usm.bluetube.videolist.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Thumbnails(
    val medium: ThumbnailAttributes = ThumbnailAttributes()
)