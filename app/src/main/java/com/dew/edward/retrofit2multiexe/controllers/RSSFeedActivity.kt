package com.dew.edward.retrofit2multiexe.controllers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.widget.Toast
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.adapters.RSSListViewAdapter
import com.dew.edward.retrofit2multiexe.apis.VogellaAPI
import com.dew.edward.retrofit2multiexe.models.Article
import com.dew.edward.retrofit2multiexe.models.RSSFeed
import kotlinx.android.synthetic.main.activity_rss_feed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory




class RSSFeedActivity : AppCompatActivity(), Callback<RSSFeed> {


    private val BASE_URL = "http://vogella.com/"
    private val articleList = arrayListOf<Article>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rss_feed)

        listViewRSSFeed.adapter = RSSListViewAdapter(articleList)
        val divider = DividerItemDecoration(listViewRSSFeed.context,
                                            DividerItemDecoration.HORIZONTAL)
        listViewRSSFeed.addItemDecoration(divider)

        start()
    }

    private fun start(){
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()

        val vogellaAPI = retrofit.create(VogellaAPI::class.java)
        val call: Call<RSSFeed> = vogellaAPI.loadRSSFeed()
        call.enqueue(this)
    }

    override fun onFailure(call: Call<RSSFeed>?, t: Throwable?) {
       t?.printStackTrace()
    }

    override fun onResponse(call: Call<RSSFeed>?, response: Response<RSSFeed>?) {
         if (response != null && response.isSuccessful){
             val rss = response.body()
             if (rss != null) {
                 textTitle.text = rss.channelTitle

                 articleList.addAll(rss.articleList!!)
                 listViewRSSFeed.adapter.notifyDataSetChanged()

             } else {
                 Toast.makeText(this, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
             }
         }
    }
}
