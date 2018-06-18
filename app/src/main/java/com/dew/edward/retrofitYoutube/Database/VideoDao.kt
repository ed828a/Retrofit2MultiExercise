package com.dew.edward.retrofitYoutube.Database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.dew.edward.retrofitYoutube.model.VideoModel

@Dao
interface VideoDao {

    // call this everytime when fetch videos from network
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addVideos(posts: List<VideoModel>)

    // call this everytime when UI wants contains to show.
    @Query("SELECT * FROM videos ORDER BY title ASC")
    fun getVideos(): LiveData<List<VideoModel>>

    // call this everytime when a new query starts
    @Query("DELETE FROM videos")
    fun resetTable()
}