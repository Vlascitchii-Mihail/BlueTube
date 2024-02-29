package com.usm.bluetube.videolist.model

data class VideosPage(
    val etag: String,
    val kind: String,
    val nextPageToken: String,
    val prevPageToken: String,
    val pageInfo: PageInfo,
    val videoItems: List<VideoItem>
)