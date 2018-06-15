package com.dew.edward.retrofitYoutube.deserializer

import com.dew.edward.retrofitYoutube.model.VideoModel
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

class VideoModelDeserializer: JsonDeserializer<List<VideoModel>> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?,
                             context: JsonDeserializationContext?): List<VideoModel> {
        val videoList = arrayListOf<VideoModel>()
        if (json != null){
            val youtubeJsonObject = json.asJsonObject
            val itemsJsonArray = youtubeJsonObject.get("items").asJsonArray
            for (i in 0 until itemsJsonArray.size()){
                var videoId = ""
                var publishedAt = ""
                var thumbnail = ""
                var title = ""

                val videoJsonObject = itemsJsonArray[i] as JsonObject
                if (videoJsonObject.has("id")){
                    val idJsonObject = youtubeJsonObject.get("id").asJsonObject
                    if (idJsonObject.has("kind") && idJsonObject.get("kind").asString == "youtube#video"){
                        if (idJsonObject.has("videoId"))
                            videoId = idJsonObject.get("videoId").asString

                        val snippetJsonObject = videoJsonObject.get("snippet").asJsonObject
                        title = snippetJsonObject.get("title").asString
                        publishedAt = snippetJsonObject.get("publishedAt").asString
                        val thumbnailJsonObject = snippetJsonObject.get("thumbnails").asJsonObject
                        val highJsonObject = thumbnailJsonObject.get("high").asJsonObject
                        thumbnail = highJsonObject.get("url").asString
                    }
                }
                val videoModel = VideoModel(title, publishedAt, thumbnail, videoId)
                videoList.add(videoModel)
            }
        }

        return videoList
    }

}