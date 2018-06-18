package com.dew.edward.retrofitYoutube.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context

class ViewModelFactory (private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VideoViewModel::class.java)){
            return VideoViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}