package com.usm.bluetube.videolist.screen

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoList : BaseFragment<FragmentVideoListBinding>(FragmentVideoListBinding::inflate) {

    private val videoListAdapter: VideoListAdapter by lazy { VideoListAdapter() }
    private val videListViewModel: VideoListViewModel by viewModels()

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
                    R.id.ic_user -> Toast.makeText(requireContext(), "User icon", Toast.LENGTH_LONG).show()
                    R.id.ic_notify_bell -> Toast.makeText(requireContext(), "Notify", Toast.LENGTH_LONG).show()
                    R.id.ic_search_video -> Toast.makeText(requireContext(), "Search", Toast.LENGTH_LONG).show()
                }
                return false
            }
        }, viewLifecycleOwner)
    }

    private fun setupObservers() = with(videListViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                youtubeResponse.collect { youtubeResponse ->
                    videoListAdapter.submitList(youtubeResponse.items)
                    Log.d("VideoList", "setupObservers() called with: youtubeResponse = ${youtubeResponse.items}")
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                errorMsgResponse.collect { errorMsg ->
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                    Log.d("ErrorMessage", errorMsg)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding.rvVideoList) {
            layoutManager = LinearLayoutManager(context)
            adapter = videoListAdapter
            videListViewModel.fetchVideos()
        }
    }
}