package com.usm.bluetube.videolist.screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.usm.bluetube.databinding.VideoListItemBinding
import com.usm.bluetube.util.setImage
import com.usm.bluetube.videolist.model.YoutubeVideo

class VideoListAdapter(): ListAdapter<YoutubeVideo, VideoListAdapter.VideoPreviewViewHolder>(ListItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPreviewViewHolder {
        val binding = VideoListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return VideoPreviewViewHolder(binding)
    }

    override fun onBindViewHolder(videPreviewViewHolder: VideoPreviewViewHolder, position: Int) {
        videPreviewViewHolder.bind(getItem(position))
    }


    inner class VideoPreviewViewHolder(private val binding: VideoListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(videoPreview: YoutubeVideo) = with(binding) {
            youtubeVideo = videoPreview
            imgPreview.setImage(videoPreview.snippet.thumbnails.medium.url)
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