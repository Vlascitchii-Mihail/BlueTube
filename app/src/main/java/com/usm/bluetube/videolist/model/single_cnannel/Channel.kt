package com.usm.bluetube.videolist.model.single_cnannel

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Channel(
    val id: String = "",
    val snippet: ChannelContent = ChannelContent()
)
