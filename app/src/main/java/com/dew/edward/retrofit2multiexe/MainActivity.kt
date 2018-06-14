package com.dew.edward.retrofit2multiexe

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.dew.edward.retrofit2multiexe.controllers.*

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.menu_credentials -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.buttonQueryGerrit -> {
                val intent = Intent(this, GerritActivity::class.java)
                startActivity(intent)
            }

            R.id.buttonRSSfeed -> {
                val intent = Intent(this, RSSFeedActivity::class.java)
                startActivity(intent)
            }

            R.id.buttonStackOverflow -> {
                val intent = Intent(this, StackActivity::class.java)
                startActivity(intent)
            }

            R.id.buttonGithubApi -> {
                val intent = Intent(this, GithubActivity::class.java)
                startActivity(intent)
            }

            R.id.buttonTwitter -> {
                val intent = Intent(this, TwitterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
