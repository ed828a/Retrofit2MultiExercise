package com.dew.edward.retrofitYoutube.model

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

data class SearchResult(
        val videoList: LiveData<PagedList<VideoModel>>,
        val networkError: LiveData<String>)