package com.usm.bluetube.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.usm.bluetube.BaseFragment
import com.usm.bluetube.R
import com.usm.bluetube.databinding.FragmentVideoListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoList : BaseFragment<FragmentVideoListBinding>(FragmentVideoListBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       setupView()
    }

    private fun setupView() {
        setupAppBar()
        setupToolbarMenu()
    }

    private fun setupAppBar() = with(binding) {
        val appCompatActivity  = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolBar)
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
}