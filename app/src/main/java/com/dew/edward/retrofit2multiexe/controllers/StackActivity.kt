package com.dew.edward.retrofit2multiexe.controllers

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.adapters.StackListViewAdapter
import com.dew.edward.retrofit2multiexe.apis.ListWrapper
import com.dew.edward.retrofit2multiexe.apis.StackOverflowAPI
import com.dew.edward.retrofit2multiexe.models.Answer
import com.dew.edward.retrofit2multiexe.models.Question
import com.dew.edward.retrofit2multiexe.providers.FakeDataProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_stack.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StackActivity : AppCompatActivity(), View.OnClickListener {

    val BASE_URL = "https://api.stackexchange.com"

    val answers = arrayListOf<Answer>()
    val questions = arrayListOf<Question>()

    private var token: String? = null
    lateinit var stackOverflowAPI: StackOverflowAPI
    lateinit var recyclerViewAdapter: StackListViewAdapter

    val questionsCallback = object : Callback<ListWrapper<Question>>{
        override fun onFailure(call: Call<ListWrapper<Question>>?, t: Throwable?) {
            t?.printStackTrace()
        }

        override fun onResponse(call: Call<ListWrapper<Question>>?, response: Response<ListWrapper<Question>>?) {
            if (response != null && response.isSuccessful) {
                val questionsList = response.body()
                val arrayAdapter = ArrayAdapter<Question>(this@StackActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        questionsList?.items)
                spinnerQuestion.adapter = arrayAdapter
            } else {
                Log.d("QuestionsCallback", "Code: ${response?.code()} Message:${response?.message()}")
            }
        }
    }

    val answersCallback = object : Callback<ListWrapper<Answer>>{
        override fun onFailure(call: Call<ListWrapper<Answer>>?, t: Throwable?) {
            t?.printStackTrace()
        }

        override fun onResponse(call: Call<ListWrapper<Answer>>?, response: Response<ListWrapper<Answer>>?) {
            if (response != null && response.isSuccessful) {
                val data = ArrayList<Answer>()
//                data.addAll(response.body()!!.items)
//                recyclerViewStack.adapter = StackListViewAdapter(data)

                answers.clear()
                answers.addAll(response.body()!!.items)
                recyclerViewAdapter.notifyDataSetChanged()
            } else {
                Log.d("AnswersCallback", "Code: ${response?.code()} Message:${response?.message()}")
            }
        }
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack)

        spinnerQuestion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@StackActivity, "Spinner Item $position selected.", Toast.LENGTH_SHORT).show()
                val question: Question = parent?.adapter?.getItem(position) as Question
                stackOverflowAPI.getAnswersForQueston(question.questionId).enqueue(answersCallback)
            }

        }

        questions.addAll(FakeDataProvider.getQuestions())
        val arrayAdapter = ArrayAdapter<Question>(this@StackActivity,
                android.R.layout.simple_spinner_dropdown_item,
                questions)
        spinnerQuestion.adapter = arrayAdapter

        answers.addAll(FakeDataProvider.getAnswers())
        recyclerViewStack.setHasFixedSize(true)
        recyclerViewAdapter = StackListViewAdapter(answers)
        recyclerViewStack.adapter = recyclerViewAdapter

        createStackOverflowAPI()
        stackOverflowAPI.getQuestions().enqueue(questionsCallback)
    }

    private fun createStackOverflowAPI(){
        val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        stackOverflowAPI = retrofit.create(StackOverflowAPI::class.java)
    }

    override fun onResume() {
        super.onResume()
        if (token != null) {
            buttonAuthentication.isEnabled = false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id){
            android.R.id.text1 -> {
                if (token != null) {
                    Toast.makeText(this@StackActivity, "recyclerView item selected.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@StackActivity, "You need to authenticate first.", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.buttonAuthentication -> Toast.makeText(this@StackActivity, "Authentication button clicked.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            token = data?.getStringExtra("token")
        }
    }





}

