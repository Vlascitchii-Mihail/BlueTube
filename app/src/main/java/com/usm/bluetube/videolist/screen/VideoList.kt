package com.usm.bluetube.videolist.screen

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.usm.bluetube.BaseFragment
import com.usm.bluetube.R
import com.usm.bluetube.databinding.FragmentVideoListBinding
import com.usm.bluetube.util.setImage
import com.usm.bluetube.videolist.model.single_cnannel.YoutubeChannelResponse
import com.usm.bluetube.videolist.model.videos.YoutubeVideoResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoList : BaseFragment<FragmentVideoListBinding>(FragmentVideoListBinding::inflate) {

    private val viewModel: VideoListViewModel by viewModels()

    private val videoListAdapter: VideoListAdapter by lazy {
        VideoListAdapter (::fetchChannelImage) { channelImageView: ImageView, url: String ->
            channelImageView.setImage(url, requireContext())
        }
    }

    private fun fetchChannelImage(channelId: String, channelImageView: ImageView, itemPosition: Int) {
        val channelCache = viewModel.channelResponse.replayCache
        when(channelCache.isNotEmpty()) {
            true -> {
                val channel = channelCache[itemPosition].values.first().items.first()
                if (channel.id == channelId) {
                    val channelUrl = channel.snippet.thumbnails.medium.url
                    channelImageView.setImage(channelUrl, requireContext())
                } else {
                    viewModel.fetchChannelImage(channelId, channelImageView)
                }
            }
            false -> viewModel.fetchChannelImage(channelId, channelImageView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       setupView()
    }

    private fun setupView() {
        setupAppBar()
        setupToolbarMenu()
        setupObservers()
        setupRecyclerView()
    }

    private fun setupAppBar() = with(binding) {
        val appCompatActivity  = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(tbVideoList)
    }

    private fun setupToolbarMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    //todo: perform different actions on menu item selected
                    R.id.ic_user -> Toast.makeText(requireContext(), "User icon", Toast.LENGTH_LONG).show()
                    R.id.ic_notify_bell -> Toast.makeText(requireContext(), "Notify", Toast.LENGTH_LONG).show()
                    R.id.ic_search_video -> Toast.makeText(requireContext(), "Search", Toast.LENGTH_LONG).show()
                }
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun setupObservers() = with(viewModel) {

        doOnCollectFrom(youtubeVideoResponse) { youtubeResponse: YoutubeVideoResponse ->
            videoListAdapter.submitList(youtubeResponse.items)
        }

        doOnCollectFrom(errorMsgResponse) { errorMsg: String ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        }

        doOnCollectFrom(channelResponse) { channelResponse: Map<ImageView, YoutubeChannelResponse> ->
            channelResponse.keys.first().setImage(
                channelResponse.values.first().items.first().snippet.thumbnails.medium.url,
                requireContext()
            )
        }
    }

    private fun <T> doOnCollectFrom(listener: Flow<T>, action: (T) -> Unit) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                listener.collect { listenedData ->
                    action(listenedData)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding.rvVideoList) {
            layoutManager = LinearLayoutManager(context)
            adapter = videoListAdapter
            if (viewModel.youtubeVideoResponse.value.items.isEmpty()) {
                viewModel.fetchVideos()
            }
        }
    }

}