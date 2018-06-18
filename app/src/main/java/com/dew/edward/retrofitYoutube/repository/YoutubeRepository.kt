package com.dew.edward.retrofitYoutube.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dew.edward.retrofitYoutube.Database.VideoCacheDatabase
import com.dew.edward.retrofitYoutube.model.SearchResult
import com.dew.edward.retrofitYoutube.model.VideoModel
import com.dew.edward.retrofitYoutube.network.NetworkService
import com.dew.edward.retrofitYoutube.util.App
import com.dew.edward.retrofitYoutube.util.BROADCAST_VIDEOLIST_DATA_CHANGED
import java.util.concurrent.Executors

//2018-02-19T22:56:12.000Z
class YoutubeRepository(val context: Context) {

    val videoDb = VideoCacheDatabase.getInstance(context)
    val videoDao = videoDb.videoDao()

    val networkService = NetworkService()

    private val networkErrors = MutableLiveData<String>()
    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false
    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1



    /**
     * Insert a list of repos in the database, on a background thread.
     */
    private fun insertVideos(videos: List<VideoModel>) {
        Executors.newSingleThreadExecutor().execute {
            Log.d("InsertVideos", "inserting ${videos.size} Videos")
            videoDao.addVideos(videos)
        }
    }

    private fun loadVideos(name: String = ""): LiveData<List<VideoModel>> {
        // appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return videoDao.getVideos()
    }


    private val requestSuccess = fun(videos: ArrayList<VideoModel>) {
        insertVideos(videos)
        isRequestInProgress = false
    }

    private val requestError = fun(error: String) {
        Log.d("Error", "Request failed, Error: $error")
        networkErrors.postValue(error)
        isRequestInProgress = false
    }


    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        if (query.isEmpty()) {
            networkService.getPopularVideos(requestSuccess, requestError)
        } else {
            networkService.searchVideos(query, requestSuccess, requestError)
        }
    }

    fun search(query: String = ""): SearchResult {
        Log.d("YoutubeRepository", "New query: $query")
        Executors.newSingleThreadExecutor().execute {
            videoDao.resetTable()
        }
        lastRequestedPage = 1
        requestAndSaveData(query)

        // Get data from the local cache
        val data = loadVideos()

        return SearchResult(data, networkErrors)
    }

    fun requestMore(query: String) {
        requestAndSaveData(query)
    }
}