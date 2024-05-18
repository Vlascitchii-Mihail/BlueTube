package com.usm.bluetube.shortslist.screen

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.usm.bluetube.BaseFragment
import com.usm.bluetube.R
import com.usm.bluetube.core.core_api.Constants.Companion.FIRST_ELEMENT
import com.usm.bluetube.core.core_api.Constants.Companion.PLAYER_LOAD_VIDEO_DELAY
import com.usm.bluetube.core.core_paging.VideoLoadStateAdapter
import com.usm.bluetube.databinding.FragmentShortsListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShortsList : BaseFragment<FragmentShortsListBinding>(FragmentShortsListBinding::inflate) {

    private val viewModel: ShortsViewModel by viewModels()
    private val shortsPlayerQueue: MutableList<YouTubePlayer?> = mutableListOf()

    private val shortsAdapter: ShortsAdapter by lazy {
        ShortsAdapter(viewModel.viewModelScope, lifecycle) { player: YouTubePlayer? ->
            shortsPlayerQueue.add(player)
        }.apply {
            addLoadStateListener { loadState ->
                with(binding) {
                    vpShortsList.isVisible = loadState.source.refresh is LoadState.NotLoading
                    pbListLoadState.isVisible = loadState.source.refresh is LoadState.Loading
                    btnRetryLoad.isVisible = loadState.source.refresh is LoadState.Error
                    tvErrorList.isVisible = loadState.source.refresh is LoadState.Error
                    showToastOnError(loadState)
                    setRetryListAction(btnRetryLoad)
                }
            }
        }
    }

    private fun showToastOnError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(),
                requireContext().getString(R.string.wrong_internet), Toast.LENGTH_LONG).show()
        }
    }

    private fun setRetryListAction(button: Button) {
        button.setOnClickListener {
            shortsAdapter.refresh()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        setupViewPager()
        setupObservers()
    }
    private fun setupObservers() {
        setupPagingFlowObserver()
        setupOnShortShowListener()
    }

    private fun setupPagingFlowObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getShorts().collectLatest { newVideos ->
                    shortsAdapter.submitData(newVideos)
                }
            }
        }
    }

    private fun setupOnShortShowListener() {
        binding.vpShortsList.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.viewModelScope.launch {
                    delay(PLAYER_LOAD_VIDEO_DELAY)
                    playLoadedVideo()
                }
            }
        })
    }

    private fun playLoadedVideo() {
        if (shortsPlayerQueue.isNotEmpty()) {
            shortsPlayerQueue.first()?.play()
            shortsPlayerQueue.removeAt(FIRST_ELEMENT)
        }
    }

    private fun setupViewPager() {
        binding.vpShortsList.adapter = shortsAdapter
            .withLoadStateHeaderAndFooter(
                header = VideoLoadStateAdapter { shortsAdapter.retry() },
                footer = VideoLoadStateAdapter { shortsAdapter.retry() }
            )
    }
}