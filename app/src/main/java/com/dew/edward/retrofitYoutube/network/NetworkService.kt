package com.dew.edward.retrofitYoutube.network

import android.util.Log
import com.dew.edward.retrofitYoutube.api.YoutubeAPI
import com.dew.edward.retrofitYoutube.model.PopularResponseModel
import com.dew.edward.retrofitYoutube.model.SearchResponseModel
import com.dew.edward.retrofitYoutube.model.VideoModel
import com.dew.edward.retrofitYoutube.util.App
import com.dew.edward.retrofitYoutube.util.YOUTUBE_BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkService {
    //    val videosCache = arrayListOf<VideoModel>()
    val videosCache = App.videos
    var nextPageToken: String = ""
    var isRequestInProgress = false
    var lastQuery = ""

    fun getPopularVideos(
            onSuccess: (videos: ArrayList<VideoModel>) -> Unit,
            onError: (error: String) -> Unit) {

        if (!isRequestInProgress) {  // idle

            isRequestInProgress = true

            val retrofit = Retrofit.Builder()
                    .baseUrl(YOUTUBE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            val youtubeAPI = retrofit.create(YoutubeAPI::class.java)
            val call = if (nextPageToken.isEmpty()) {
                youtubeAPI.getPopularVideos()
            } else {
                youtubeAPI.getPopularVideos(nextPageToken)
            }
            call.enqueue(object : Callback<PopularResponseModel> {
                override fun onFailure(call: Call<PopularResponseModel>?, t: Throwable?) {
                    t?.printStackTrace()
                    isRequestInProgress = false
                    nextPageToken = ""
                    onError(t?.message ?: "unknown error")
                    Log.d("searchVideosCallback", "searching video on Youtube failed.")
                }

                override fun onResponse(call: Call<PopularResponseModel>?,
                                        response: Response<PopularResponseModel>?) {

                    if (response != null && response.isSuccessful) {
                        val videosResponse: PopularResponseModel = response.body()!!
                        nextPageToken = videosResponse.nextPageToken
                        videosCache.clear()
                        videosCache.addAll(videosResponse.items.map {
                            VideoModel(it.snippet.title ?: "",
                                    it.snippet.publishedAt.extractDate() ?: "",
                                    it.snippet.thumbnails.high.url ?: "",
                                    it.id ?: "",
                                    it.statistics.viewCount ?: "")
                        })
                        Log.d("popularVideoCallback", "Video Cache: $videosCache")
                        onSuccess(videosCache)
                    } else {
                        nextPageToken = ""
                        Log.d("popularVideoCallback", "Code: ${response?.code()}, Message: ${response?.message()}")
                        onError(response?.errorBody()?.string() ?: "Unknown error")
                    }
                    isRequestInProgress = false
                }
            })
        }
    }

    fun searchVideos(query: String,
                     onSuccess: (videos: ArrayList<VideoModel>) -> Unit,
                     onError: (error: String) -> Unit) {

        if (!isRequestInProgress) {  // idle

            isRequestInProgress = true

            val retrofit = Retrofit.Builder()
                    .baseUrl(YOUTUBE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            val youtubeAPI = retrofit.create(YoutubeAPI::class.java)
            val call = if (nextPageToken.isEmpty()) {
                lastQuery = query
                youtubeAPI.searchVideoQuery(query)
            } else {
                Log.d("SearchVideos", "lastQuery: $lastQuery--NextPageToken: $nextPageToken")
                youtubeAPI.searchVideoQuery(query = lastQuery, pageToken = nextPageToken)
            }
            call.enqueue(object : Callback<SearchResponseModel> {
                override fun onFailure(call: Call<SearchResponseModel>?, t: Throwable?) {
                    t?.printStackTrace()
                    isRequestInProgress = false
                    nextPageToken = ""
                    onError(t?.message ?: "unknown error")
                    Log.d("searchVideosCallback", "searching video on Youtube failed.")
                }

                override fun onResponse(call: Call<SearchResponseModel>?,
                                        response: Response<SearchResponseModel>?) {
                    if (response != null && response.isSuccessful) {
                        val videosResponse: SearchResponseModel = response.body()!!
                        nextPageToken = videosResponse.nextPageToken
                        videosCache.clear()
                        videosCache.addAll(videosResponse.items.map {
                            VideoModel(
                                    it.snippet.title ?: "",
                                    it.snippet.publishedAt.extractDate() ?: "",
                                    it.snippet.thumbnails.high.url ?: "",
                                    it.id.videoId ?: "")
                        })
                        Log.d("searchVideosCallback", "Video Cache: $videosCache")
                        onSuccess(videosCache)
                    } else {
                        nextPageToken = ""
                        Log.d("searchVideosCallback", "Code: ${response?.code()}, Message: ${response?.message()}")
                        onError(response?.errorBody()?.string() ?: "Unknown error")
                    }
                    isRequestInProgress = false
                }
            })
        }
    }
}


fun String.extractDate(): String {
    val stringArray = this.split('T')
    return stringArray[0]
}