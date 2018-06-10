package com.dew.edward.retrofit2multiexe.apis

import com.dew.edward.retrofit2multiexe.models.RSSFeed
import retrofit2.Call
import retrofit2.http.GET

/*
 * Created by Edward on 6/10/2018.
 */

interface VogellaAPI {

    @GET("article.rss")
    fun loadRSSFeed(): Call<RSSFeed>
}