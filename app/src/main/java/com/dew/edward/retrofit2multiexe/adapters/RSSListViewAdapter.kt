package com.dew.edward.retrofit2multiexe.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.models.Article
import kotlinx.android.synthetic.main.raw_rss_list.view.*
/*
 * Created by Edward on 6/10/2018.
 */

class RSSListViewAdapter(val dataList: List<Article>):
        RecyclerView.Adapter<RSSListViewAdapter.RSSViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RSSViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.raw_rss_list, parent, false)

        return RSSViewHolder(view)
    }

    override fun getItemCount() = dataList.count()

    override fun onBindViewHolder(holder: RSSViewHolder, position: Int) {
        val article = dataList[position]
        holder.textTitle?.text = article.title
        holder.textLink?.text = article.link
    }

    inner class RSSViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        val textTitle = itemView?.textRawTitle
        val textLink = itemView?.textRawLink
    }
}