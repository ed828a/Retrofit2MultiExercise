package com.dew.edward.retrofitYoutube.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.dew.edward.retrofitYoutube.model.SearchResult
import com.dew.edward.retrofitYoutube.repository.YoutubeRepository
import com.dew.edward.retrofitYoutube.util.VISIBLE_THRESHOLD
import android.arch.lifecycle.Transformations
import com.dew.edward.retrofitYoutube.model.VideoModel

class VideoViewModel(context: Context): ViewModel() {

    private val repository = YoutubeRepository(context)
    var lastQuery = ""

    private val queryLiveData = MutableLiveData<String>()
    private val searchResult: LiveData<SearchResult> = Transformations.map(queryLiveData) {
        repository.search(it)
    }

    val videoList: LiveData<List<VideoModel>> = Transformations.switchMap(searchResult) {
        searchResult -> searchResult.videoList
    }
    val networkError: LiveData<String> = Transformations.switchMap(searchResult) {
        searchResult -> searchResult.networkError
    }


    /**
     * Search a repository based on a query string.
     */
    fun searchVideos(queryString: String = "") {
        lastQuery = queryString
        queryLiveData.postValue(queryString) // equivalent to repository.search(queryString)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
                repository.requestMore(lastQuery)
        }
    }

    fun lastQueryValue(): String? = queryLiveData.value
}