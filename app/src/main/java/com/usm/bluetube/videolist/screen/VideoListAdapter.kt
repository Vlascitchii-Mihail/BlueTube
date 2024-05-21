package com.usm.bluetube.videolist.screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.usm.bluetube.core.core_util.setImage
import com.usm.bluetube.databinding.VideoListItemBinding
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import kotlinx.coroutines.CoroutineScope

class VideoListAdapter(
    private val viewModelScope: CoroutineScope,
    private val navigateToPlayer: (YoutubeVideo) -> Unit
) : PagingDataAdapter<YoutubeVideo, VideoListAdapter.VideoPreviewViewHolder>(ListItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPreviewViewHolder {
        val binding = VideoListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return VideoPreviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoPreviewViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class VideoPreviewViewHolder(private val binding: VideoListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(videoPreview: YoutubeVideo) = with(binding) {
            youtubeVideo = videoPreview
            imgPreview.setImage(
                videoPreview.snippet.thumbnails.medium.url,
                imgPreview.context,
                viewModelScope
            )
            imgPreview.setOnClickListener { navigateToPlayer.invoke(videoPreview) }
            imgChannel.setImage(
                videoPreview.snippet.channelImgUrl,
                imgChannel.context,
                viewModelScope
            )
        }
    }

    class ListItemCallback: DiffUtil.ItemCallback<YoutubeVideo>() {
        override fun areItemsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: YoutubeVideo, newItem: YoutubeVideo): Boolean {
            return oldItem == newItem
        }
    }
}
