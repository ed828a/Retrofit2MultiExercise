package com.dew.edward.retrofitYoutube.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.dew.edward.retrofitYoutube.repository.YoutubeRepository
import com.dew.edward.retrofitYoutube.util.App
import com.dew.edward.retrofitYoutube.util.VISIBLE_THRESHOLD

class VideoViewModel(context: Context): ViewModel() {

    val repository = YoutubeRepository(context)
    var lastQuery = ""
    val searchResultVideos = App.videoLoadFromDB

    /**
     * Search a repository based on a query string.
     */
    fun searchVideos(queryString: String = "") {
        lastQuery = queryString

        repository.search(queryString)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
                repository.requestMore(lastQuery)
        }
    }

}