package com.usm.bluetube.core.core_paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.usm.bluetube.databinding.ItemLoadStateBinding

class VideoLoadStateAdapter(
    private val retry: () -> Unit
): LoadStateAdapter<VideoLoadStateAdapter.VideoLoadStateViewHolder>() {

    inner class VideoLoadStateViewHolder(
        private val binding: ItemLoadStateBinding,
        refresh: () -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetryLoad.setOnClickListener { refresh.invoke() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                pbListLoadState.isVisible = loadState is LoadState.Loading
                btnRetryLoad.isVisible = loadState !is LoadState.Loading
                tvErrorList.isVisible = loadState !is LoadState.Loading
                if (loadState is LoadState.Error) tvErrorList.text = loadState.error.localizedMessage
            }
        }
    }

    override fun onBindViewHolder(holder: VideoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): VideoLoadStateViewHolder {
        val binding = ItemLoadStateBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return VideoLoadStateViewHolder(binding, retry)
    }
}