package com.tribuanabagus.belajarbahasainggris.view.main.ui.student.tutorial.play_video

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.HttpDataSource.HttpDataSourceException
import com.google.android.exoplayer2.upstream.HttpDataSource.InvalidResponseCodeException
import com.tribuanabagus.belajarbahasainggris.databinding.FragmentPlayVideoBinding
import com.tribuanabagus.belajarbahasainggris.local_db.VideoPembelajaran
import com.tribuanabagus.belajarbahasainggris.view.dialog.LoadingDialogFragment.Companion.TAG


class PlayVideoFragment : Fragment(), Player.Listener {

    private var _binding: FragmentPlayVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var player: ExoPlayer

    private val urlVideo = "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"

    private lateinit var data: VideoPembelajaran

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()

        player.addListener(this)
    }

    private fun prepareView() {
        val data = PlayVideoFragmentArgs.fromBundle(arguments as Bundle).data
        // Create a data source factory.
//        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        // Create a SmoothStreaming media source pointing to a manifest uri.
//        val mediaSource = SsMediaSource.Factory(dataSourceFactory)
//            .createMediaSource(MediaItem.fromUri(data.url))

        object : YouTubeExtractor(requireContext()) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>, vMeta: VideoMeta) {
                if (ytFiles != null) {
//                    ytFiles.
                }
            }
        }.extract(data.url, true, false)

        player = ExoPlayer.Builder(requireContext()).build()
        binding.styledPlayerView.player = player
        // Build the media item.
        val mediaItem =
            MediaItem.fromUri(urlVideo)
        // Set the media item to be played.
        player.setMediaItem(mediaItem)
//        player.setMediaSource(mediaSource)
        // Prepare the player.
        player.prepare()
        // Start the playback.
        player.play()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        Log.d(TAG, "onIsPlayingChanged: $isPlaying")
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

}