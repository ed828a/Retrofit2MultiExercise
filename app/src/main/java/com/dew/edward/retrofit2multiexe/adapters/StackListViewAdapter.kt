package com.dew.edward.retrofit2multiexe.adapters

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.models.Answer
import com.dew.edward.retrofit2multiexe.module.GlideApp
import kotlinx.android.synthetic.main.raw_stack_answer.view.*
/*
 * Created by Edward on 6/11/2018.
 */

class StackListViewAdapter(val answers: List<Answer>) : RecyclerView.Adapter<StackListViewAdapter.StackViewHolder>(){
    var counter = 0
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.raw_stack_answer, parent, false)
        mContext = parent.context
        counter++
        if (counter % 2 == 0){
            view.setBackgroundColor(Color.GRAY)
        }
        Log.v("ViewType", "viewType = $viewType")
        return StackViewHolder(view)
    }

    override fun getItemCount() = answers.count()

    override fun onBindViewHolder(holder: StackViewHolder, position: Int) {
       val answer = answers[position]
        holder.textItem?.text = answer.toString()
        if (holder.profileImage != null) {
            Log.d("ProfileImage", "answer.owner.profileImage = ${answer.owner?.profileImage}")
            if (answer.owner?.profileImage != null) {
                GlideApp.with(mContext)
                        .load(Uri.parse(answer.owner?.profileImage))
                        .into(holder.profileImage)
            }
        }
    }

    inner class StackViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val textItem: TextView? = itemView?.textAnswer
        val profileImage = itemView?.imageProfile
    }
}