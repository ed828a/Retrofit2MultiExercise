package com.dew.edward.retrofit2multiexe.deserializer

import com.dew.edward.retrofit2multiexe.models.GithubRepo
import com.google.gson.*
import java.lang.reflect.Type

/*
 * Created by Edward on 6/13/2018.
 */

class GithubRepoDeserializer: JsonDeserializer<GithubRepo> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type?,
                             context: JsonDeserializationContext?): GithubRepo {

        return if (json != null){
            val repoJsonObject: JsonObject = json.asJsonObject
            val name: String = repoJsonObject.get("name").asString
            val url: String = repoJsonObject.get("url").asString

            val ownerJsonElement = repoJsonObject.get("owner")
            val ownerJsonObject = ownerJsonElement.asJsonObject
            val owner: String = ownerJsonObject.get("login").asString

            GithubRepo(name, owner, url)
        } else {
            GithubRepo()
        }
    }
}