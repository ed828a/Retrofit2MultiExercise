package com.dew.edward.retrofit2multiexe.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dew.edward.retrofit2multiexe.models.Answer

/*
 * Created by Edward on 6/11/2018.
 */

class StackListViewAdapter(val answers: List<Answer>) : RecyclerView.Adapter<StackListViewAdapter.StackViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)

        return StackViewHolder(view)
    }

    override fun getItemCount() = answers.count()

    override fun onBindViewHolder(holder: StackViewHolder, position: Int) {
       val answer = answers[position]
        holder.textItem?.text = answer.toString()
    }

    inner class StackViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textItem: TextView? = itemView?.findViewById(android.R.id.text1)

    }
}