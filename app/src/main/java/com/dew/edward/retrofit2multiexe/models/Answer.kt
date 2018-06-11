package com.dew.edward.retrofit2multiexe.models

/*
 * Created by Edward on 6/11/2018.
 */

import com.google.gson.annotations.SerializedName
import retrofit2.http.Path

class Answer(
        @SerializedName("answer_id")
        var answerId: Int,

        @SerializedName("is_accepted")
        var accepted: Boolean,

        var score: Int,

        @SerializedName("owner")
        var owner: Answer.Owner? = null) {

    override fun toString(): String {
        return "${answerId.toString()} - Score: $score - Accepted: ${if (accepted) "Yes" else "No"}"
    }

    class Owner (
            @SerializedName("profile_image")
            var profileImage: String)
}
