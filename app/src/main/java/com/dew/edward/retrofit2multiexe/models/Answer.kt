package com.dew.edward.retrofit2multiexe.models

/*
 * Created by Edward on 6/11/2018.
 */

import com.google.gson.annotations.SerializedName

class Answer(
        @SerializedName("answer_id")
        var answerId: Int,

        @SerializedName("is_accepted")
        var accepted: Boolean,

        var score: Int) {

    override fun toString(): String {
        return "${answerId.toString()} - Score: $score - Accepted: ${if (accepted) "Yes" else "No"}"
    }
}
