package com.dew.edward.retrofitYoutube.api

// first page https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&order=relevance&q=site%3Ayoutube.com&topicId=%2Fm%2F02vx4&key=AIzaSyA7cdJ8OPftCtkqIBpVuIX5CVtY7BW02JU
//after first page https://www.googleapis.com/youtube/v3/search?pageToken=CBkQAA&part=snippet&maxResults=25&order=relevance&q=site%3Ayoutube.com&topicId=%2Fm%2F02vx4&key=AIzaSyA7cdJ8OPftCtkqIBpVuIX5CVtY7BW02JU

import com.dew.edward.retrofitYoutube.model.PopularResponseModel
import com.dew.edward.retrofitYoutube.model.SearchResponseModel
import com.dew.edward.retrofitYoutube.util.API_KEY
import com.dew.edward.retrofitYoutube.util.NETWORK_PAGE_SIZE
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface YoutubeAPI {

    @GET("search")
    fun searchVideo(@Query("q") query: String = "",
                    @Query("pageToken") pageToken: String = "",
                    @Query("part") part: String = "snippet",
                    @Query("maxResults") maxResults: String = "$NETWORK_PAGE_SIZE",
                    @Query("type") type: String = "video",
                    @Query("key") key: String = API_KEY): Call<SearchResponseModel>


    @GET("videos?part=snippet%2CcontentDetails%2Cstatistics&chart=mostPopular&maxResults=$NETWORK_PAGE_SIZE&regionCode=AU&key=$API_KEY")
    fun getPopularVideos(@Query("pageToken") nextPage: String = ""): Call<PopularResponseModel>

    @GET("search?part=snippet&order=relevance&key=$API_KEY")
    fun searchVideoQuery(@Query("q") query: String = "",
                         @Query("maxResults") itemsPerPage: String = "$NETWORK_PAGE_SIZE",
                         @Query("pageToken") pageToken: String = ""): Call<SearchResponseModel>


}
