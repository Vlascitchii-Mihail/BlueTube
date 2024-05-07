package com.usm.bluetube.core.core_util

sealed class VideoType {
    data object Videos: VideoType()

    class SearchedVideo(var query: String): VideoType()
}