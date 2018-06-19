package com.dew.edward.retrofitYoutube.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dew.edward.retrofitYoutube.Database.VideoCacheDatabase
import com.dew.edward.retrofitYoutube.model.SearchResult
import com.dew.edward.retrofitYoutube.model.VideoModel
import com.dew.edward.retrofitYoutube.network.NetworkService
import com.dew.edward.retrofitYoutube.util.App
import com.dew.edward.retrofitYoutube.util.BROADCAST_VIDEOLIST_DATA_CHANGED
import com.dew.edward.retrofitYoutube.util.DATABASE_PAGE_SIZE
import java.util.concurrent.Executors

//2018-02-19T22:56:12.000Z
class YoutubeRepository(val context: Context) {

    private val _networkError = MutableLiveData<String>()
    val networkError: LiveData<String>
        get() = _networkError

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    val networkService = NetworkService()
    val videoDb = VideoCacheDatabase.getInstance(context)
    val videoDao = videoDb.videoDao()

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        if (query.isEmpty()) {
            networkService.getPopularVideos(requestSuccess, requestError)
        } else {
            networkService.searchVideos(query, requestSuccess, requestError)
        }
    }

    private val requestSuccess = fun(videos: ArrayList<VideoModel>) {
        insertVideos(videos)
        isRequestInProgress = false
    }

    private val requestError = fun(error: String) {
        Log.d("Error", "Request failed, Error: $error")
        _networkError.postValue(error)
        isRequestInProgress = false
    }

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    private fun insertVideos(videos: List<VideoModel>) {
        Executors.newSingleThreadExecutor().execute {
            Log.d("InsertVideos", "inserting ${videos.size} Videos")
            videoDao.addVideos(videos)
        }
    }

    private fun loadVideos(name: String = ""): DataSource.Factory<Int, VideoModel> {
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return videoDao.getVideos()
    }

    fun search(query: String = ""): SearchResult {
        Log.d("YoutubeRepository", "New query: $query")
        Executors.newSingleThreadExecutor().execute {
            videoDao.resetTable()
        }

        requestAndSaveData(query)

        // Get DataSource Factory from the local cache
        val dataSourceFactory = loadVideos()
        val boundaryCallback = VideoCallback(query)
        // Get data from the local cache
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        return SearchResult(data, networkError)
    }

    inner class VideoCallback(val query: String = ""): PagedList.BoundaryCallback<VideoModel>(){
        override fun onZeroItemsLoaded() {
            requestAndSaveData(query)
        }

        override fun onItemAtEndLoaded(itemAtEnd: VideoModel) {
            requestAndSaveData(query)
        }
    }
}