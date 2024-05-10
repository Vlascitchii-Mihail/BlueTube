package com.usm.bluetube.videolist.screen

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.usm.bluetube.BaseFragment
import com.usm.bluetube.R
import com.usm.bluetube.core.core_api.Constants.Companion.INPUT_DELAY
import com.usm.bluetube.databinding.FragmentVideoListBinding
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import com.usm.bluetube.core.core_paging.VideoLoadStateAdapter
import com.usm.bluetube.core.core_util.setupBackground
import com.usm.bluetube.core.core_util.setupTextAppearance
import com.usm.bluetube.search_video.SearchInputDelay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoList : BaseFragment<FragmentVideoListBinding>(FragmentVideoListBinding::inflate) {

    private val viewModel: VideoListViewModel by viewModels()
    private val searchInputDelay = SearchInputDelay()

    private val videoListAdapter: VideoListAdapter by lazy {
        VideoListAdapter(viewModel.viewModelScope) { youtubeVideo: YoutubeVideo ->
            findNavController().navigate(
                VideoListDirections.actionVideoListFragmentToVideoPlayerFragment(youtubeVideo)
            )
        }.apply {
            addLoadStateListener { loadState ->
                with(binding) {
                    rvVideoList.isVisible = loadState.source.refresh is LoadState.NotLoading
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
            videoListAdapter.refresh()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       setupView()
    }

    private fun setupView() {
        setupAppBar()
        setupToolbarMenu()
        listenTo(viewModel.videoFlow)
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
                val searchView = menu.findItem(R.id.ic_search_video).actionView as SearchView
                searchView.setupTextAppearance(requireContext(), R.font.roboto_medium_500)
                searchView.setupBackground(requireContext(), R.drawable.searchview_background)
                searchView.setupSearchAction()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun SearchView.setupSearchAction() {
        this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                makeDelayedQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                makeDelayedQuery(newText)
                return false
            }
        })
    }

    private fun makeDelayedQuery(newText: String?) {
        searchInputDelay.setInputDelay(
            INPUT_DELAY,
            viewModel.viewModelScope
        ) {
            if (newText != null && newText != "") {
                listenTo(viewModel.getSearchVideosFlow(newText))
            }
        }
    }

    private fun listenTo(videoFlow: StateFlow<PagingData<YoutubeVideo>>) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                videoFlow.collectLatest { newVideos ->
                    videoListAdapter.submitData(newVideos)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding.rvVideoList) {
            layoutManager = LinearLayoutManager(context)
            adapter = videoListAdapter.withLoadStateHeaderAndFooter(
                header = VideoLoadStateAdapter { videoListAdapter.retry() },
                footer = VideoLoadStateAdapter { videoListAdapter.retry() }
            )
        }
    }
}
