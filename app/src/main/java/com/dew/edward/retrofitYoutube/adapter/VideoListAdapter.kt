package com.dew.edward.retrofitYoutube.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.module.GlideApp
import com.dew.edward.retrofitYoutube.model.VideoModel
import kotlinx.android.synthetic.main.raw_video.view.*

class VideoListAdapter : ListAdapter<VideoModel, VideoListAdapter.VideoListViewHolder>(VIDEO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_video, parent, false)

        return VideoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        val videoModel = getItem(position)
        holder.publishedAtView?.text = videoModel.publishedAt
        holder.titleView?.text = videoModel.title
        if (videoModel.viewCount.isNotEmpty()) {
            holder.viewCountView?.text = videoModel.viewCount + " views"
        } else {
            holder.viewCountView?.text = ""
        }
        GlideApp.with(holder.itemView.context).load(videoModel.thumbnail).centerCrop().into(holder.thumbView!!)
    }

    inner class VideoListViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val thumbView = itemView?.imageThumb
        val titleView = itemView?.textTitle
        val publishedAtView = itemView?.textPublishedAt
        val viewCountView = itemView?.textViewCount
    }

    companion object {
        private val VIDEO_COMPARATOR = object : DiffUtil.ItemCallback<VideoModel>() {
            override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean =
                    oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean =
                    oldItem.videoId == newItem.videoId
        }
    }
}