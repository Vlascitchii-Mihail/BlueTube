package com.usm.bluetube.ui

import android.os.Bundle
import android.view.View
import com.usm.bluetube.BaseFragment
import com.usm.bluetube.databinding.FragmentVideoListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoList : BaseFragment<FragmentVideoListBinding>(FragmentVideoListBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}