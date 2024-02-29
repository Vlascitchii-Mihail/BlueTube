package com.usm.bluetube.videolist.screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.usm.bluetube.databinding.VideoListItemBinding
import com.usm.bluetube.videolist.model.VideoItem

class VideoListAdapter(): ListAdapter<VideoItem, VideoListAdapter.VideoPreviewViewHolder>(ListItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPreviewViewHolder {
        val binding = VideoListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return VideoPreviewViewHolder(binding)
    }

    override fun onBindViewHolder(videPreviewViewHolder: VideoPreviewViewHolder, position: Int) {
        videPreviewViewHolder.bind(getItem(position))
    }


    inner class VideoPreviewViewHolder(private val binding: VideoListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(videoItem: VideoItem) = with(binding) {
            //todo bind the video list's item
        }
    }
    class ListItemCallback: DiffUtil.ItemCallback<VideoItem>() {
        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem == newItem
        }
    }
}