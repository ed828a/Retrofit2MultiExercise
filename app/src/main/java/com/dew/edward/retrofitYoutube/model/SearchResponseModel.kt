package com.dew.edward.retrofitYoutube.model

class SearchResponseModel (val nextPageToken: String,
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