package com.dew.edward.retrofit2multiexe.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.apis.TwitterAPI
import com.dew.edward.retrofit2multiexe.models.OAuthToken
import com.dew.edward.retrofit2multiexe.models.UserDetails
import com.dew.edward.retrofit2multiexe.module.TWITTER_BASE_URL
import kotlinx.android.synthetic.main.activity_twitter.*
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TwitterActivity : AppCompatActivity() {

    var token: OAuthToken? = null
    private val credentials = Credentials.basic("jMePoCRxNC1dzEvZcZNdTQOEz",
            "ehCFPvpu7pDs14sRoebBipwUuUf51UBHI9rJTqv3aRY2W0i1sg")
    lateinit var twitterAPI: TwitterAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twitter)

        createTwitterAPI()

        requestTokenButton.setOnClickListener {
            twitterAPI.postCredentials("client_credentials").enqueue(tokenCallback)
        }

        requestUserDetailsButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            if (username.isNotEmpty())
                twitterAPI.getUserDetails(username).enqueue(userDetailsCallback)
            else
                Toast.makeText(this@TwitterActivity, "please provide a username", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createTwitterAPI() {
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain: Interceptor.Chain ->
                    val originalRequest: Request = chain.request()
                    val builder: Request.Builder = originalRequest.newBuilder()
                            .header("Authorization", token?.authorization ?: credentials)

                    val newRequest = builder.build()
                    chain.proceed(newRequest)
                }
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(TWITTER_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        twitterAPI = retrofit.create(TwitterAPI::class.java)
    }

    private val tokenCallback = object : Callback<OAuthToken> {
        override fun onFailure(call: Call<OAuthToken>?, t: Throwable?) {
            t?.printStackTrace()
        }

        override fun onResponse(call: Call<OAuthToken>?, response: Response<OAuthToken>?) {
            if (response != null && response.isSuccessful) {
                requestTokenButton.isEnabled = false
                requestUserDetailsButton.isEnabled = true
                usernameEditText.isEnabled = true
                usernameTextView.isEnabled = true
                token = response.body()
            } else {
                Toast.makeText(this@TwitterActivity, "Failure while requesting token", Toast.LENGTH_SHORT).show()
                Log.d("TokenCallback", "Code: ${response?.code()} Message: ${response?.message()}")
            }
        }
    }

    private val userDetailsCallback = object : Callback<UserDetails> {
        override fun onFailure(call: Call<UserDetails>?, t: Throwable?) {
            t?.printStackTrace()
        }

        override fun onResponse(call: Call<UserDetails>?, response: Response<UserDetails>?) {
            if (response != null && response.isSuccessful) {
                val userDetails = response.body()
                nameTextView.text = userDetails?.name ?: "No value"
                locationTextView.text = userDetails?.location ?: "No value"
                urlTextView.text = userDetails?.url ?: "No value"
                descriptionTextView.text = userDetails?.description ?: "No value"
            } else {
                Toast.makeText(this@TwitterActivity, "Failure while requesting user details", Toast.LENGTH_SHORT).show()
                Log.d("UserDetailsCallback", "Code: ${response?.code()} Message: ${response?.message()}")
            }
        }
    }


}
