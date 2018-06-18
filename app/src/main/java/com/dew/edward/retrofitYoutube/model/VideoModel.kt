package com.dew.edward.retrofitYoutube.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoModel(
        var title: String = "",
        var publishedAt: String = "",
        var thumbnail: String = "",
        @PrimaryKey var videoId: String = "",
        var viewCount: String = "")


