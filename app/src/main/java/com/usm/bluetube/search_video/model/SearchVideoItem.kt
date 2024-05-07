package com.usm.bluetube.search_video.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import com.usm.bluetube.videolist.model.videos.VideoSnippet
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class SearchVideoItem(
    val id: SearchVideoItemId,
    val snippet: VideoSnippet
): Parcelable
