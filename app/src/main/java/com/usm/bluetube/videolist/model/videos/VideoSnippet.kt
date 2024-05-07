package com.usm.bluetube.videolist.model.videos

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.usm.bluetube.videolist.model.Thumbnails
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class VideoSnippet(
    val title: String,
    val description: String,
    val publishedAt: String,
    val channelTitle: String,
    var channelImgUrl: String = "",
    val channelId: String,
    val thumbnails: Thumbnails
): Parcelable
