package com.usm.bluetube.videolist.screen

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       setupView()
    }

    private fun setupView() {
        setupAppBar()
        setupToolbarMenu()
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

    private fun setupRecyclerView() {
        with(binding.rvVideoList) {
            layoutManager = LinearLayoutManager(context)

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    adapter = videoListAdapter
                }
            }
        }
    }
}