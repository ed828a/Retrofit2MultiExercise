package com.dew.edward.retrofit2multiexe.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.apis.GerritAPI
import com.dew.edward.retrofit2multiexe.models.Change
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_gerrit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GerritActivity : AppCompatActivity(), Callback<List<Change>> {

    private val BASE_URL = "https://git.eclipse.org/r/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gerrit)

        start()
    }

    private fun start() {
        val gson: Gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        val gerritAPI = retrofit.create(GerritAPI::class.java)

        val call: Call<List<Change>> = gerritAPI.loadChanges("status:open")
        call.enqueue(this)
    }

    override fun onFailure(call: Call<List<Change>>?, t: Throwable?) {
        t?.printStackTrace()
    }

    override fun onResponse(call: Call<List<Change>>?, response: Response<List<Change>>?) {
        if (response != null && response.isSuccessful){
            val changesList = response.body()
            val subjects = changesList?.map{it.subject}
            if (changesList != null){
                val adapter = ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, subjects)
                listGerrit.adapter = adapter
                changesList.forEach { println(it.subject) }
            }
        }
    }
}
