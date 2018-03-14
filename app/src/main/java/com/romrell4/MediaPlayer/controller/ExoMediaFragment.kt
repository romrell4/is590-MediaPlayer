package com.romrell4.MediaPlayer.controller

import android.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.view.*
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.romrell4.MediaPlayer.R
import com.romrell4.MediaPlayer.support.inflateV2

/**
 * Created by romrell4 on 3/13/18
 */
class ExoMediaFragment: Fragment() {
    companion object {
        private const val VIDEO_URL_EXTRA = "videoUrl"
        private const val AUDIO_URL_EXTRA = "audioUrl"

        fun newInstance(videoUrl: String, audioUrl: String): ExoMediaFragment {
            val fragment = ExoMediaFragment()
            val bundle = Bundle()
            bundle.putString(VIDEO_URL_EXTRA, videoUrl)
            bundle.putString(AUDIO_URL_EXTRA, audioUrl)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var exoPlayerView: SimpleExoPlayerView? = null
    private lateinit var exoPlayer: SimpleExoPlayer
    private var videoMode = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflateV2(R.layout.fragment_exo_media, container)

        setHasOptionsMenu(true)

        exoPlayerView = view?.findViewById<SimpleExoPlayerView>(R.id.playerView)
        exoPlayer = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(activity), DefaultTrackSelector(), DefaultLoadControl())
        exoPlayerView?.player = exoPlayer
        exoPlayer.playWhenReady = true

        setupPlayer()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.media, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        R.id.toggle_media_type -> {
            item.title = if (videoMode) "Video" else "Audio"
            videoMode = !videoMode
            setupPlayer()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupPlayer() {
        val uri = Uri.parse(arguments.getString(if (videoMode) VIDEO_URL_EXTRA else AUDIO_URL_EXTRA))
        val mediaSource = ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory("romrell4")).createMediaSource(uri)
        exoPlayer.prepare(mediaSource)
    }
}