package com.dew.edward.retrofit2multiexe.apis

import com.dew.edward.retrofit2multiexe.models.GithubIssue
import com.dew.edward.retrofit2multiexe.models.GithubRepo
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/*
 * Created by Edward on 6/13/2018.
 */

public interface GithubAPI {

    @GET("user/repos?per_page=100")
    fun getRepos(): Single<List<GithubRepo>>

    @GET("/repos/{owner}/{repo}/issues")
    fun getIssues(@Path("owner") owner: String,
                  @Path("repo") repository: String): Single<List<GithubIssue>>

    @POST
    fun postComment(@Url url: String,
                    @Body issue: GithubIssue): Single<ResponseBody>
}