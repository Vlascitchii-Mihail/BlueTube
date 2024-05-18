package com.usm.bluetube.core.core_util

sealed class VideoType {

    data object Videos: VideoType()
    data object Shorts: VideoType()

    class SearchedVideo(var query: String): VideoType()
}