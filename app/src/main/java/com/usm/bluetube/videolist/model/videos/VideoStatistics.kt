package com.usm.bluetube.videolist.model.videos

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class VideoStatistics(
    val viewCount: Long = 0,
    val likeCount: Long = 0,
): Parcelable