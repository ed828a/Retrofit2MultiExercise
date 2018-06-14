package com.dew.edward.retrofit2multiexe.models

import com.google.gson.annotations.SerializedName

/*
 * Created by Edward on 6/13/2018.
 */

class GithubIssue(var id: String = "",
                  var title: String = "",
                  var comments_url: String = "",

                  @SerializedName("body")
                  var comment: String = "") {

    override fun toString(): String {
        return "$id - $title"
    }
}

