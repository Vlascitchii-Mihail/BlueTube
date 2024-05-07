package com.usm.bluetube.shortslist.screen

import android.os.Bundle
import android.view.View
import com.usm.bluetube.BaseFragment
import com.usm.bluetube.databinding.FragmentShortsListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShortsList : BaseFragment<FragmentShortsListBinding>(FragmentShortsListBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}