package com.dew.edward.retrofitYoutube.model


class PopularResponseModel(val nextPageToken: String,
                    val items: List<Item> = arrayListOf()){
    class Item(val id: String, val snippet: Snippet, val statistics: Statistics){
        class Snippet(val publishedAt: String, val title: String, val thumbnails: Thumbnails){
            class Thumbnails(val high: High) {
                class High( val url: String)
            }
        }
        class Statistics(val viewCount: String)
    }
}