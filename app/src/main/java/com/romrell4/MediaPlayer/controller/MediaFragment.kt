package com.romrell4.MediaPlayer.controller

import android.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.MediaController
import android.widget.VideoView
import com.romrell4.MediaPlayer.R
import com.romrell4.MediaPlayer.support.CachingProxy
import com.romrell4.MediaPlayer.support.inflateV2

/**
 * Created by romrell4 on 3/4/18
 */
class MediaFragment: Fragment() {
    companion object {
        private const val VIDEO_URL_EXTRA = "videoUrl"
        private const val AUDIO_URL_EXTRA = "audioUrl"

        fun newInstance(videoUrl: String, audioUrl: String): MediaFragment {
            val fragment = MediaFragment()
            val bundle = Bundle()
            bundle.putString(VIDEO_URL_EXTRA, videoUrl)
            bundle.putString(AUDIO_URL_EXTRA, audioUrl)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var videoMode = true
    private lateinit var mediaController: MediaController
    private var videoView: VideoView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        val view = inflater?.inflateV2(R.layout.fragment_video, container)
        videoView = view?.findViewById<VideoView>(R.id.videoView)
        mediaController = MediaController(activity)
        mediaController.setAnchorView(videoView)
        videoView?.setMediaController(mediaController)

        setupMediaPlayer()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.media, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId) {
        R.id.toggle_media_type -> {
//            item.title = if (videoMode) "Video" else "Audio"
//            videoMode = !videoMode
//            setupMediaPlayer()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupMediaPlayer() {
        videoView?.pause()
        val proxy = CachingProxy.getInstance(activity)
        videoView?.setVideoPath(proxy.getProxyUrl(arguments.getString(if (videoMode) VIDEO_URL_EXTRA else AUDIO_URL_EXTRA)))
        videoView?.start()
    }
}