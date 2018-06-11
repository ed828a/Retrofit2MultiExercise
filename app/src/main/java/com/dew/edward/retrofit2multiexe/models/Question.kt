package com.dew.edward.retrofit2multiexe.models

/*
 * Created by Edward on 6/11/2018.
 */

import com.google.gson.annotations.SerializedName

class Question (var title: String,
                var body: String,

                @SerializedName("question_id")
                var questionId: String
                ){

    override fun toString(): String {
        return title
    }
}
