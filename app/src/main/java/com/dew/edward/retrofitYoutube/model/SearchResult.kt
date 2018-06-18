package com.dew.edward.retrofitYoutube.model

import android.arch.lifecycle.LiveData

data class SearchResult(
        val videoList: LiveData<List<VideoModel>>,
        val networkError: LiveData<String>)