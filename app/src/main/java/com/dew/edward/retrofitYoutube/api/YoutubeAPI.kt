package com.dew.edward.retrofitYoutube.api

// first page https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&order=relevance&q=site%3Ayoutube.com&topicId=%2Fm%2F02vx4&key=AIzaSyA7cdJ8OPftCtkqIBpVuIX5CVtY7BW02JU
//after first page https://www.googleapis.com/youtube/v3/search?pageToken=CBkQAA&part=snippet&maxResults=25&order=relevance&q=site%3Ayoutube.com&topicId=%2Fm%2F02vx4&key=AIzaSyA7cdJ8OPftCtkqIBpVuIX5CVtY7BW02JU

import com.dew.edward.retrofitYoutube.model.VideoModel
import com.dew.edward.retrofitYoutube.model.YoutubeResponseModel
import com.dew.edward.retrofitYoutube.util.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface YoutubeAPI {


    @GET("search")
    fun searchVideo(@Query("q") query: String = "",
                    @Query("pageToken") pageToken: String = "",
                    @Query("part") part: String = "snippet",
                    @Query("maxResults") maxResults: String = "25",
                    @Query("type") type: String = "video",
                    @Query("key") key: String = API_KEY): Call<YoutubeResponseModel>


    @GET("videos?part=contentDetails&chart=mostPopular&regionCode=IN&maxResults=25&key=$API_KEY")
    fun getPupularVideo(): Call<List<VideoModel>>

    @GET("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults={maxResults}&order=relevance&q={q}&key=$API_KEY")
    fun searchVideoPath(@Path("q") query: String,
                        @Path("maxResults") itemsPerPage: String): Call<List<VideoModel>>

    @GET("/search?pageToken={pageToken}&part=snippet&maxResults={maxResults}&order=relevance&q={q}&key=$API_KEY")
    fun searchVideoMorePagePath(@Path("q") query: String,
                                @Path("pageToken") pageToken: String,
                                @Path("maxResults") itemsPerPage: String): Call<List<VideoModel>>

    @GET("https://www.googleapis.com/youtube/v3/search")
    fun searchVideoQuery(@Query("key") apiKey: String,
                         @Query("q") query: String,
                         @Query("maxResults") itemsPerPage: String,
                         @Query("part") videoPart: String = "snippet"): Call<List<VideoModel>>

    @GET("/search?pageToken={pageToken}&part=snippet&maxResults={maxResults}&order=relevance&q={q}&key=$API_KEY")
    fun searchVideoMorePageQuery(@Query("key") apiKey: String,
                                 @Query("pageToken") pageToken: String,
                                 @Query("maxResults") itemsPerPage: String,
                                 @Query("part") videoPart: String = "snippet"): Call<List<VideoModel>>


}
