package com.dew.edward.retrofit2multiexe.models

/*
 * Created by Edward on 6/13/2018.
 */

class GithubRepo(var name: String = "", var owner: String = "", var url: String = "") {
    override fun toString(): String {
        return "$name  $url"
    }
}