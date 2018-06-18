package com.dew.edward.retrofitYoutube.controller


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.R.id.recyclerVideoList
import com.dew.edward.retrofit2multiexe.R.id.search_repo
import com.dew.edward.retrofitYoutube.adapter.VideoListAdapter
import com.dew.edward.retrofitYoutube.model.VideoModel
import com.dew.edward.retrofitYoutube.network.NetworkService
import com.dew.edward.retrofitYoutube.util.App
import com.dew.edward.retrofitYoutube.util.DEFAULT_QUERY
import com.dew.edward.retrofitYoutube.util.LAST_SEARCH_QUERY
import com.dew.edward.retrofitYoutube.viewmodel.VideoViewModel
import com.dew.edward.retrofitYoutube.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_youtube_list.*

class YoutubeListActivity : AppCompatActivity() {

    val networkService = NetworkService()
    lateinit var videoViewModel: VideoViewModel
    var morePageControl = 0
    var receivingCount = 0
    private val adapter = VideoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_list)


        videoViewModel = ViewModelProviders.of(this, ViewModelFactory(this))
                                            .get(VideoViewModel::class.java)
        recyclerVideoList.adapter = adapter
        videoViewModel.videoList.observe(this, Observer<List<VideoModel>> {
            Log.d("Activity", "list: ${it?.size}")
            adapter.submitList(it)
        })
        videoViewModel.networkError.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops ${it}", Toast.LENGTH_LONG).show()
        })


//        App.localBroadcastManager.registerReceiver(videoListDataChangedReceiver,
//                IntentFilter(BROADCAST_VIDEOLIST_DATA_CHANGED))

        setupScrollListener()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        videoViewModel.searchVideos(query)
        initSearch(query)
    }

    private fun initAdapter(){
        recyclerVideoList.adapter = adapter
        videoViewModel.videoList.observe(this, Observer<List<VideoModel>> {
            Log.d("Activity", "list: ${it?.size}")
            adapter.submitList(it)
        })
        videoViewModel.networkError.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops ${it}", Toast.LENGTH_LONG).show()
        })
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

    private fun initSearch(query: String) {
        search_repo.setText(query)

        search_repo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        search_repo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        search_repo.text.trim().let {
            if (it.isNotEmpty()) {
                recyclerVideoList.scrollToPosition(0)
                videoViewModel.searchVideos(it.toString())
                adapter.submitList(null)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, videoViewModel.lastQueryValue())
    }
    override fun onDestroy() {

        App.localBroadcastManager.unregisterReceiver(videoListDataChangedReceiver)
        super.onDestroy()
    }
}
