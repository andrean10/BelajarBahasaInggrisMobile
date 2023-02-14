package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial.play_video

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.HttpDataSource.HttpDataSourceException
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentPlayVideoBinding
import com.tribuanabagus.belajarbahasainggris.network.ApiConfig
import com.tribuanabagus.belajarbahasainggris.view.dialog.LoadingDialogFragment.Companion.TAG
import com.tribuanabagus.belajarbahasainggris.view.main.ui.student.StudentActivity


class PlayVideoFragment : Fragment(), Player.Listener {

    private var _binding: FragmentPlayVideoBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainActivity: StudentActivity

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as StudentActivity
        _binding = FragmentPlayVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
    }

    private fun prepareView() {
        val data = PlayVideoFragmentArgs.fromBundle(arguments as Bundle).data

        // Create a data source factory.
//        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        // Create a SmoothStreaming media source pointing to a manifest uri.
//        val mediaSource = SsMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(MediaItem.fromUri(data.url))

        player = ExoPlayer.Builder(requireContext()).build()
        binding.styledPlayerView.player = player

        // Build the media item.
        if (data.video != null && player != null) {
            val mediaItem =
                MediaItem.fromUri(ApiConfig.URL_FILES + data.video)
//         Set the media item to be played.
            player!!.setMediaItem(mediaItem)
//            player.setMediaSource(mediaSource)
//         Prepare the player.
            player!!.prepare()
//         Start the playback.
            player!!.play()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentMediaItemIndex
            player?.release()
            player = null
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        Log.d(TAG, "onIsPlayingChanged: $isPlaying")
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        Log.d(TAG, "onPlaybackStateChanged: $playbackState")

//        with(binding) {
//            if (playbackState == Player.STATE_READY) {
//                pbLoading.visibility = View.INVISIBLE
//            } else {
//                pbLoading.visibility = View.VISIBLE
//            }
//        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        with(binding) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //First Hide other objects (listview or recyclerview), better hide them using Gone.
                val params = styledPlayerView.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                styledPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                styledPlayerView.layoutParams = params
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //unhide your objects here.
                val params = styledPlayerView.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                styledPlayerView.layoutParams = params
            }
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        val cause = error.cause
        Log.d(TAG, "onPlayerError: $cause")
        if (cause is HttpDataSourceException) {
            Log.d(TAG, "onPlayerError:  An HTTP error occurred.")
            // An HTTP error occurred.
            // It's possible to find out more about the error both by casting and by
            // querying the cause.
            if (cause is InvalidResponseCodeException) {
                Log.d(
                    TAG,
                    "onPlayerError: Cast to InvalidResponseCodeException and retrieve the response code"
                )
                // Cast to InvalidResponseCodeException and retrieve the response code,
                // message and headers.
            } else {
                Log.d(TAG, "onPlayerError: kecuali di atas")
                // Try calling httpError.getCause() to retrieve the underlying cause,
                // although note that it may be null.
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.mediaPlayer.pause()
    }


    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.mediaPlayer.start()
    }
}