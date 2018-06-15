package com.dew.edward.retrofitYoutube.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.module.GlideApp
import com.dew.edward.retrofitYoutube.util.App
import kotlinx.android.synthetic.main.raw_video.view.*

class VideoListAdapter: RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder>() {
    val videoList = App.videos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_video, parent, false)

        return VideoListViewHolder(view)
    }

    override fun getItemCount() = videoList.count()

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        val videoModel = videoList[position]
        holder.publishedAtView?.text = videoModel.publishedAt
        holder.titleView?.text = videoModel.title
        GlideApp.with(holder.itemView.context).load(videoModel.thumbnail).centerCrop().into(holder.thumbView!!)
    }

    inner class VideoListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val thumbView = itemView?.imageThumb
        val titleView = itemView?.textTitle
        val publishedAtView = itemView?.textPublishedAt
    }
}