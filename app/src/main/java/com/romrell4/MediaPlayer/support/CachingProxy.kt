package com.romrell4.MediaPlayer.support

import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer

/**
 * Created by romrell4 on 3/13/18
 */
class CachingProxy private constructor() {
    companion object {
        private var instance: HttpProxyCacheServer? = null

        fun getInstance(context: Context): HttpProxyCacheServer {
            return if (instance != null) instance!! else HttpProxyCacheServer(context).also { instance = it }
        }
    }
}