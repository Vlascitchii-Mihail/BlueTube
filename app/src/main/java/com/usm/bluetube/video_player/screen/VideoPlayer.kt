package com.usm.bluetube.video_player.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.usm.bluetube.BaseFragment
import com.usm.bluetube.R
import com.usm.bluetube.core.core_paging.VideoLoadStateAdapter
import com.usm.bluetube.core.core_util.setImage
import com.usm.bluetube.databinding.FragmentPlayVideoBinding
import com.usm.bluetube.videolist.model.videos.YoutubeVideo
import com.usm.bluetube.videolist.screen.VideoListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VideoPlayer : BaseFragment<FragmentPlayVideoBinding>(FragmentPlayVideoBinding::inflate) {

    private val viewModel: VideoPlayerViewModel by viewModels()
    private lateinit var relatedVideoFlow: StateFlow<PagingData<YoutubeVideo>>
    private val args: VideoPlayerArgs by navArgs()
    private var isFullScreen = false
    private lateinit var youTubePlayer: YouTubePlayer
    private val landscapeOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    private val landscapeSensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    private val portraitUserOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
    private val sensorOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

    private val videoListAdapter: VideoListAdapter by lazy {
        VideoListAdapter(viewModel.viewModelScope) { youtubeVideo: YoutubeVideo ->
            findNavController().navigate(
                VideoPlayerDirections.openNewVideo(youtubeVideo)
            )
        }.apply {
            addLoadStateListener { loadState ->
                with(binding) {
                    rvRelatedVideo.isVisible = loadState.source.refresh is LoadState.NotLoading
                    pbListLoadState.isVisible = loadState.source.refresh is LoadState.Loading
                    btnRetryLoad.isVisible = loadState.source.refresh is LoadState.Error && this@apply.itemCount == 0
                    tvErrorList.isVisible = loadState.source.refresh is LoadState.Error && this@apply.itemCount == 0
                    showToastOnError(loadState)
                    setRetryListAction(binding.btnRetryLoad)
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

        setBackPressListener()
        setupFullScreenListener()
        playVideoFromId(args.video.id)
        relatedVideoFlow = viewModel.getSearchVideos(args.video.snippet.title)
        setupObservers()
        setupVideoDescription(args.video)
        setupRecyclerView()
    }

    private fun setBackPressListener() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFullScreen) youTubePlayer.toggleFullscreen()
                else findNavController().popBackStack()
            }
        })
    }

    private fun setupFullScreenListener() {
        with(binding) {
            ytPlayer.addFullscreenListener(object: FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    setFullScreenVisibility(fullscreenView)
                    changeToLandscapeOrientation(requireActivity())
                }

                override fun onExitFullscreen() {
                    setPortraitVisibility()
                    changeToPortraitOrientation(requireActivity())
                }
            })
        }
    }

    private fun setFullScreenVisibility(fullscreenView: View) {
        isFullScreen = true
        with(binding) {
            rvRelatedVideo.visibility = View.GONE
            flFullScreenContainer.apply {
                visibility = View.VISIBLE
                addView(fullscreenView)
            }
        }
    }

    private fun setPortraitVisibility() {
        isFullScreen = false
        with(binding) {
            flFullScreenContainer.apply {
                visibility = View.GONE
                removeAllViews()
            }
            rvRelatedVideo.visibility = View.VISIBLE
        }
    }

    private fun changeToLandscapeOrientation(activity: Activity) {
        if (activity.requestedOrientation != landscapeOrientation)
            activity.requestedOrientation = landscapeSensorOrientation
    }

    private fun changeToPortraitOrientation(activity: Activity) {
        if (activity.requestedOrientation != sensorOrientation) {
            activity.requestedOrientation = portraitUserOrientation
            activity.requestedOrientation = sensorOrientation
        }
    }

    private fun playVideoFromId(videoId: String) {
        registerPlayerObserver()

        binding.ytPlayer.enableAutomaticInitialization = false
        val fullScreenControl = IFramePlayerOptions.Builder().controls(1).fullscreen(1).build()
        binding.ytPlayer.initialize(getYouTubePlayerListener(videoId), fullScreenControl)
    }

    private fun getYouTubePlayerListener(videoId: String): AbstractYouTubePlayerListener {
        return object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@VideoPlayer.youTubePlayer = youTubePlayer
                youTubePlayer.loadOrCueVideo(lifecycle, videoId, 0F)
            }
        }
    }

    private fun registerPlayerObserver() {
        lifecycle.addObserver(binding.ytPlayer)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        when(newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                if (!isFullScreen) youTubePlayer.toggleFullscreen()
            }
            Configuration.ORIENTATION_PORTRAIT -> {
                if (isFullScreen) youTubePlayer.toggleFullscreen()
            }
            else -> {}
        }
    }

    private fun setupRecyclerView() {
        with(binding.rvRelatedVideo) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = videoListAdapter.withLoadStateHeaderAndFooter(
                header = VideoLoadStateAdapter { videoListAdapter.retry() },
                footer = VideoLoadStateAdapter { videoListAdapter.retry() }
            )
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                relatedVideoFlow.collectLatest { newVideos ->
                    videoListAdapter.submitData(newVideos)
                }
            }
        }
    }

    private fun setupVideoDescription(video: YoutubeVideo) = with(binding){
        youtubeVideo = video
        imgChannel.setImage(video.snippet.thumbnails.medium.url, requireContext(), viewModel.viewModelScope)
    }
}