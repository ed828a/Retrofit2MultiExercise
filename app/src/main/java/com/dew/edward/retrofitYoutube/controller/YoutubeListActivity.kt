package com.dew.edward.retrofitYoutube.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofitYoutube.adapter.VideoListAdapter
import com.dew.edward.retrofitYoutube.repository.YoutubeRepository
import com.dew.edward.retrofitYoutube.util.App
import com.dew.edward.retrofitYoutube.util.BROADCAST_VIDEOLIST_DATA_CHANGED
import kotlinx.android.synthetic.main.activity_youtube_list.*

class YoutubeListActivity : AppCompatActivity() {

    val repository = YoutubeRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_list)

        recyclerVideoList.adapter = VideoListAdapter()

        App.localBroadcastManager.registerReceiver(videoListDataChangedReceiver,
                IntentFilter(BROADCAST_VIDEOLIST_DATA_CHANGED))

        repository.getVideos()
    }

    private val videoListDataChangedReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            recyclerVideoList.adapter.notifyDataSetChanged()
        }

    }

    override fun onDestroy() {

        App.localBroadcastManager.unregisterReceiver(videoListDataChangedReceiver)
        super.onDestroy()
    }
}
