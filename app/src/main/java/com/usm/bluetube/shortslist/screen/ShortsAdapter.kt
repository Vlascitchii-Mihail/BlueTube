package com.usm.bluetube.shortslist.screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.usm.bluetube.core.core_util.setImage
import com.usm.bluetube.databinding.ItemShortsBinding
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import kotlinx.coroutines.CoroutineScope

class ShortsAdapter(
    private val viewModelScope: CoroutineScope,
    private val lifecycle: Lifecycle,
    private val addToPlayerQueue: (YouTubePlayer?) -> Unit
) : PagingDataAdapter<YoutubeVideo, ShortsAdapter.ShortsHolder>(ShortsCallback()){

    inner class ShortsHolder(
        private val binding: ItemShortsBinding,
        private val lifecycle: Lifecycle,
        private val addToPlayerQueue: (YouTubePlayer?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var shortsYoutubePlayer: YouTubePlayer? = null
        private var currentShortsId: String? = null

        init {
            lifecycle.addObserver(binding.ytPlayer)
            binding.ytPlayer.addPlayerListener()
        }

        private fun YouTubePlayerView.addPlayerListener() {
            this.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    this@ShortsHolder.shortsYoutubePlayer = youTubePlayer
                    addToPlayerQueue.invoke(youTubePlayer)
                    currentShortsId?.let { youTubePlayer.cueVideo(it, 0f) }
                }
            })
        }

        fun cueVideo(shortsId: String) {
            currentShortsId = shortsId
            shortsYoutubePlayer?.cueVideo(shortsId, 0F)
            shortsYoutubePlayer?.let {
                addToPlayerQueue.invoke(it)
            }
        }

        fun bind(shortsVideo: YoutubeVideo) = with(binding) {
            youtubeVideo = shortsVideo
            imgShortsChannel.setImage(
                shortsVideo.snippet.channelImgUrl,
                imgShortsChannel.context,
                viewModelScope
            )
        }
   }

    override fun onBindViewHolder(holder: ShortsHolder, position: Int) {
        getItem(position)?.let {
            holder.cueVideo(it.id)
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsHolder {
        val binding = ItemShortsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ShortsHolder(binding, lifecycle, addToPlayerQueue)
    }
}

class ShortsCallback: DiffUtil.ItemCallback<YoutubeVideo>() {
    override fun areItemsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
        return oldItem == newItem
    }
}