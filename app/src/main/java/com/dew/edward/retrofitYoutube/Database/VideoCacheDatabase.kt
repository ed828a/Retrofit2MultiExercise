package com.dew.edward.retrofitYoutube.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.dew.edward.retrofitYoutube.model.VideoModel


@Database(entities = [VideoModel::class], version = 1, exportSchema = false)
abstract class VideoCacheDatabase : RoomDatabase(){

    abstract fun videoDao(): VideoDao

    companion object {
        @Volatile
        private var INSTANCE: VideoCacheDatabase? = null

        fun getInstance(context: Context): VideoCacheDatabase =
                INSTANCE ?: synchronized(VideoCacheDatabase::class.java) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context): VideoCacheDatabase =
                Room.databaseBuilder(context.applicationContext,
                        VideoCacheDatabase::class.java, "videos.db")
                        .build()
    }
}