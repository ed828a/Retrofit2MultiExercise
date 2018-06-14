package com.dew.edward.retrofit2multiexe.apis

import com.dew.edward.retrofit2multiexe.models.OAuthToken
import com.dew.edward.retrofit2multiexe.models.UserDetails
import retrofit2.Call
import retrofit2.http.*

/*
 * Created by Edward on 6/14/2018.
 */

interface TwitterAPI {

    @FormUrlEncoded
    @POST("oauth2/token")
    fun postCredentials(@Field("grant_type") grantType: String): Call<OAuthToken>

    @GET("/1.1/users/show.json")
    fun getUserDetails(@Query("screen_name") name: String): Call<UserDetails>
}