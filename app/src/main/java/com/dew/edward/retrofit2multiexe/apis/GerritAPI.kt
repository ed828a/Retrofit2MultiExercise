package com.dew.edward.retrofit2multiexe.apis

import com.dew.edward.retrofit2multiexe.models.Change
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
 * Created by Edward on 6/10/2018.
 */

interface GerritAPI {
    @GET("changes/")
    fun loadChanges(@Query("q")status: String): Call<List<Change>>
}