package com.usm.bluetube.core.core_api

import com.usm.bluetube.BuildConfig

class Constants {

    companion object {

        const val API_KEY = BuildConfig.GPI_KEY

        const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
        const val LIST_OF_VIDEOS = "videos"
        const val CHANNELS = "channels"
        const val SEARCH = "search"

        const val SNIPPET = "snippet"
        const val CONTENT_DETAILS = "contentDetails"
        const val STATISTICS = "statistics"
        const val MOST_POPULAR = "mostPopular"
        const val REGION_CODE = "US"
        const val SINGLE_CHANNEL = 1
        const val RELEVANCE = "relevance"

        const val INPUT_DELAY: Long = 1000
    }
}