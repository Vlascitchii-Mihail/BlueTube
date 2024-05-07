package com.usm.bluetube.videolist.model.videos

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class YoutubeVideo(
    val id: String,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics
): Parcelable
