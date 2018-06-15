package com.dew.edward.retrofitYoutube.model
import com.google.gson.annotations.SerializedName

data class VideoModel(@field:SerializedName("title")
                 val title: String = "",

                 @field:SerializedName("publishedAt")
                 val publishedAt: String = "",

                 @field:SerializedName("thumbnails")
                 val thumbnail: String = "",

                 @field:SerializedName("videoId")
                 val videoId: String = "") {
}


class YoutubeResponseModel(val nextPageToken: String,
                           val items: List<Item> = arrayListOf()) {

    class Item (val id: ID,
                val snippet: Snippet) {

        class ID (val kind: String,
                  val videoId: String)

        class Snippet(val publishedAt: String,
                      val title: String,
                      val thumbnails: Thumbnails){

            class Thumbnails(val high: High) {

                class High( val url: String)
            }
        }
    }
}