package com.dew.edward.retrofit2multiexe.controllers

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.dew.edward.retrofit2multiexe.R
import com.dew.edward.retrofit2multiexe.R.id.*
import com.dew.edward.retrofit2multiexe.apis.GithubAPI
import com.dew.edward.retrofit2multiexe.deserializer.GithubRepoDeserializer
import com.dew.edward.retrofit2multiexe.models.GithubIssue
import com.dew.edward.retrofit2multiexe.models.GithubRepo
import com.dew.edward.retrofit2multiexe.module.GITHUB_BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_github.*
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GithubActivity : AppCompatActivity(), CredentialsDialog.ICredentialsDialogListener {

    var username = ""
    var password = ""
    lateinit var githubAPI: GithubAPI
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        buttonLoadRepositories.setOnClickListener {
            compositeDisposable.add(githubAPI.getRepos()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(repositoriesObserver)
            )
        }

        buttonSendComment.setOnClickListener {
            val newComment = editComment.text.toString()
            if (newComment.isNotEmpty()) {
                val selectedItem = spinnerIssues.selectedItem as GithubIssue
                selectedItem.comment = newComment
                compositeDisposable.add(githubAPI.postComment(selectedItem.comments_url, selectedItem)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(commentObserver))
            } else {
                Toast.makeText(this@GithubActivity, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }
        with(spinnerRepositories) {
            isEnabled = false
            adapter = ArrayAdapter<String>(this@GithubActivity,
                    android.R.layout.simple_spinner_dropdown_item, arrayListOf("No repositories available"))

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (parent?.selectedItem is GithubRepo) {
                        val githubRepo = parent.selectedItem as GithubRepo
                        compositeDisposable.add(githubAPI.getIssues(githubRepo.owner, githubRepo.name)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeWith(issuesObserver))
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        with(spinnerIssues) {
            isEnabled = false
            adapter = ArrayAdapter<String>(this@GithubActivity,
                    android.R.layout.simple_spinner_dropdown_item, arrayListOf("Please select repository"))
        }

        createGithubAPI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        return when (item?.itemId) {
            R.id.menu_credentials -> {
                showCredentialsDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogPositiveClick(username: String, password: String) {
        this.username = username
        this.password = password
        buttonLoadRepositories.isEnabled = true
    }

    override fun onStop() {
        super.onStop()
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private val repositoriesObserver =
            object : DisposableSingleObserver<List<GithubRepo>>() {
                override fun onSuccess(t: List<GithubRepo>) {
                    if (t.isNotEmpty()) {
                        val spinnerAdapter = ArrayAdapter<GithubRepo>(this@GithubActivity,
                                android.R.layout.simple_spinner_dropdown_item, t)
                        spinnerRepositories.adapter = spinnerAdapter
                        spinnerRepositories.isEnabled = true
                    } else {
                        spinnerRepositories.adapter = ArrayAdapter<String>(this@GithubActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                arrayListOf("User has no repositories."))
                        spinnerRepositories.isEnabled = false
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Toast.makeText(this@GithubActivity,
                            "Can't load repositories.", Toast.LENGTH_SHORT).show()
                }
            }


    private val issuesObserver: DisposableSingleObserver<List<GithubIssue>> =
            object : DisposableSingleObserver<List<GithubIssue>>() {
                override fun onSuccess(t: List<GithubIssue>) {
                    if (t.isNotEmpty()) {
                        val adapter = ArrayAdapter<GithubIssue>(this@GithubActivity,
                                android.R.layout.simple_spinner_dropdown_item, t)
                        spinnerIssues.adapter = adapter
                        spinnerIssues.isEnabled = true
                        editComment.isEnabled = true
                        buttonSendComment.isEnabled = true
                    } else {
                        spinnerIssues.adapter = ArrayAdapter<String>(this@GithubActivity,
                                android.R.layout.simple_spinner_dropdown_item,
                                arrayListOf("Repository has no issues."))
                        spinnerIssues.isEnabled = false
                        editComment.isEnabled = false
                        buttonSendComment.isEnabled = false
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Toast.makeText(this@GithubActivity,
                            "Can't load issues.", Toast.LENGTH_SHORT).show()
                }

            }


    private val commentObserver: DisposableSingleObserver<ResponseBody> =
            object : DisposableSingleObserver<ResponseBody>() {
                override fun onSuccess(t: ResponseBody) {
                    editComment.setText("")
                    Toast.makeText(this@GithubActivity, "Comment created.", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Toast.makeText(this@GithubActivity, "Can't create comment.", Toast.LENGTH_SHORT).show()
                }
            }


    private fun createGithubAPI() {
        val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .registerTypeAdapter(GithubRepo::class.java, GithubRepoDeserializer())
                .create()

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val builder: Request.Builder = originalRequest.newBuilder().header("Authorization", Credentials.basic(username, password))
                    val newRequest = builder.build()
                    chain.proceed(newRequest)
                }
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(GITHUB_BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        githubAPI = retrofit.create(GithubAPI::class.java)
    }

    private fun showCredentialsDialog() {

        val dialog = CredentialsDialog().apply {
            arguments = Bundle().apply {
                putString("username", username)
                putString("password", password)
            }
        }
        dialog.show(supportFragmentManager, "credentialsDialog")
    }

}
