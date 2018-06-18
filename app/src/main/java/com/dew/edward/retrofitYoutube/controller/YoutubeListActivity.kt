package com.dew.edward.retrofitYoutube.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofitYoutube.adapter.VideoListAdapter
import com.dew.edward.retrofitYoutube.network.NetworkService
import com.dew.edward.retrofitYoutube.util.App
import com.dew.edward.retrofitYoutube.util.BROADCAST_VIDEOLIST_DATA_CHANGED
import com.dew.edward.retrofitYoutube.viewmodel.VideoViewModel
import kotlinx.android.synthetic.main.activity_youtube_list.*

class YoutubeListActivity : AppCompatActivity() {

    val networkService = NetworkService()
    lateinit var videoViewModel: VideoViewModel
    var morePageControl = 0
    var receivingCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_list)

        val adapter = VideoListAdapter()
        recyclerVideoList.adapter = adapter
        adapter.submitList(App.videoLoadFromDB)

        App.localBroadcastManager.registerReceiver(videoListDataChangedReceiver,
                IntentFilter(BROADCAST_VIDEOLIST_DATA_CHANGED))

        videoViewModel = VideoViewModel(this)

        videoViewModel.searchVideos()

        setupScrollListener()

    }

    private val videoListDataChangedReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            recyclerVideoList.adapter.notifyDataSetChanged()
            receivingCount++
            Log.d("videoListDataChangedReceiver", "Receiving Count: $receivingCount")
        }
    }

    private fun setupScrollListener(){
        val layoutManager = recyclerVideoList.layoutManager as LinearLayoutManager
        recyclerVideoList.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                videoViewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }


    override fun onDestroy() {

        App.localBroadcastManager.unregisterReceiver(videoListDataChangedReceiver)
        super.onDestroy()
    }
}
