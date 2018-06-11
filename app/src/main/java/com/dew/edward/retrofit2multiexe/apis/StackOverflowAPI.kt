package com.dew.edward.retrofit2multiexe.apis

import com.dew.edward.retrofit2multiexe.models.Answer
import com.dew.edward.retrofit2multiexe.models.Question
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/*
 * Created by Edward on 6/11/2018.
 */

interface StackOverflowAPI {

    @GET("/2.2/questions?order=desc&sort=votes&site=stackoverflow&tagged=android&filter=withbody")
    fun getQuestions(): Call<ListWrapper<Question>>

    @GET("/2.2/questions/{id}/answers?order=desc&sort=votes&site=stackoverflow")
    fun getAnswersForQueston(@Path("id") questionId: String): Call<ListWrapper<Answer>>

}