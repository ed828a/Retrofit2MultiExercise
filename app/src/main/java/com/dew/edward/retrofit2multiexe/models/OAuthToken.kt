package com.dew.edward.retrofit2multiexe.models

import com.google.gson.annotations.SerializedName

/*
 * Created by Edward on 6/14/2018.
 */

class OAuthToken(
        @SerializedName("access_token")
        val accessToken: String = "",

        @SerializedName("token_type")
        val tokenType: String = "") {

    val authorization: String
    get() = "$tokenType $accessToken"

}