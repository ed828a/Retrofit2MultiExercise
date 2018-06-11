package com.dew.edward.retrofit2multiexe.models;
/*
 * Created by Edward on 6/11/2018.
 */

import com.google.gson.annotations.SerializedName;

public class Answer {
    @SerializedName("answer_id")
    public int answerId;

    @SerializedName("is_accepted")
    public boolean accepted;
    public int score;

    @Override
    public String toString() {
        return answerId + " - Score: " + score + " - Accepted: " + (accepted ? "Yes" : "No");
    }
}
