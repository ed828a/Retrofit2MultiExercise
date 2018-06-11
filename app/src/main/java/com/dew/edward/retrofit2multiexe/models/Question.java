package com.dew.edward.retrofit2multiexe.models;
/*
 * Created by Edward on 6/11/2018.
 */

import com.google.gson.annotations.SerializedName;

public class Question {
    public String title;
    public String body;

    @SerializedName("question_id")
    public String questionId;

    @Override
    public String toString() {
        return title;
    }
}
