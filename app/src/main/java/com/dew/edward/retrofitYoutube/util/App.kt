package com.dew.edward.retrofitYoutube.util

import android.app.Application
import android.support.v4.content.LocalBroadcastManager
import com.dew.edward.retrofitYoutube.model.VideoModel

class App: Application() {
    companion object {
        lateinit var localBroadcastManager: LocalBroadcastManager
        lateinit var videos: ArrayList<VideoModel>
    }

    override fun onCreate() {
        super.onCreate()
        localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)
        videos = arrayListOf<VideoModel>()
    }
}