package com.dew.edward.retrofitYoutube.repository

import android.content.Intent
import android.util.Log
import com.dew.edward.retrofitYoutube.api.YoutubeAPI
import com.dew.edward.retrofitYoutube.model.PopularResponseModel
import com.dew.edward.retrofitYoutube.model.VideoModel
import com.dew.edward.retrofitYoutube.model.YoutubeResponseModel
import com.dew.edward.retrofitYoutube.util.App
import com.dew.edward.retrofitYoutube.util.BASE_URL
import com.dew.edward.retrofitYoutube.util.BROADCAST_VIDEOLIST_DATA_CHANGED
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//2018-02-19T22:56:12.000Z
class YoutubeRepository (val success: () -> Unit, val errors: () -> Unit){

    var videoNextPageToken = ""
    val videos = App.videos

    private fun createYoutubeAPI(): YoutubeAPI {
//        val gson =GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val youtubeAPI = retrofit.create(YoutubeAPI::class.java)

        return youtubeAPI
    }

    private val searchCallback = object : Callback<YoutubeResponseModel> {
        override fun onFailure(call: Call<YoutubeResponseModel>?, t: Throwable?) {
            t?.printStackTrace()
            errors()
            Log.d("YoutubeRepository", "getting video from Youtube failed.")
        }

        override fun onResponse(call: Call<YoutubeResponseModel>?, response: Response<YoutubeResponseModel>?) {
            if (response != null && response.isSuccessful) {
                val responseData: YoutubeResponseModel = response.body()!!
                videoNextPageToken = responseData.nextPageToken
                Log.d("YoutubeRepository", "videoNextPageToken: $videoNextPageToken")
                videos.addAll(responseData.items.map {
                    VideoModel(it.snippet.title, it.snippet.publishedAt, it.snippet.thumbnails.high.url, it.id.videoId)
                })
                Log.d("YoutubeRepository", "video list: $videos")
                App.localBroadcastManager.sendBroadcast(Intent(BROADCAST_VIDEOLIST_DATA_CHANGED))
            } else {
                Log.d("Callback", "Code: ${response?.code()} Message:${response?.message()}")
            }
        }
    }

    private val popularVideosCallback = object : Callback<PopularResponseModel> {
        override fun onFailure(call: Call<PopularResponseModel>?, t: Throwable?) {
            t?.printStackTrace()
            Log.d("YoutubeRepository", "getting video from Youtube failed.")
            errors()
        }

        override fun onResponse(call: Call<PopularResponseModel>?, response: Response<PopularResponseModel>?) {
            if (response != null && response.isSuccessful) {
                val responseData: PopularResponseModel = response.body()!!
                videoNextPageToken = responseData.nextPageToken
                Log.d("YoutubeRepository", "videoNextPageToken: $videoNextPageToken")
                videos.addAll(responseData.items.map {
                    VideoModel(it.snippet.title, it.snippet.publishedAt, it.snippet.thumbnails.high.url, it.id)
                })
                Log.d("YoutubeRepository", "video list: $videos")
                App.localBroadcastManager.sendBroadcast(Intent(BROADCAST_VIDEOLIST_DATA_CHANGED))
                success()
            } else {
                Log.d("Callback", "Code: ${response?.code()} Message:${response?.message()}")
            }
        }
    }

    fun getVideos() {
        val youtubeAPI = createYoutubeAPI()

//        val call = youtubeAPI.searchVideo()
//        call.enqueue(searchCallback)

        // get most popular videos
        val call = if (videoNextPageToken.isEmpty()) youtubeAPI.getPopularVideo()
        else youtubeAPI.getPopularVideo(videoNextPageToken)

        call.enqueue(popularVideosCallback)

        // get search Videos with paging
//        val call: Call<YoutubeResponseModel>
//        call = if (videoNextPageToken.isEmpty()) {
//            youtubeAPI.searchVideoQuery(query = "movie")
//
//        } else {
//            youtubeAPI.searchVideoQuery(query = "movie", pageToken = videoNextPageToken)
//        }
//        call.enqueue(searchCallback)
    }

}