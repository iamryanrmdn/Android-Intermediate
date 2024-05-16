package com.dicoding.storyapp.view.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.adapter.ListStoryAdapter
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.upload.UploadActivity
import com.dicoding.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var listStoryAdapter: ListStoryAdapter
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupSession()
        setupAction()

    }

    override fun onResume() {
        super.onResume()
        setupData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                mainViewModel.logout()
                mainViewModel.getSession().observe(this@MainActivity) {
                    Log.d(TAG, "Token: ${it.token}")
                    Log.d(TAG, "Email: ${it.email}")
                }
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setupSession() {
        mainViewModel.getSession().observe(this@MainActivity) {
            token = it.token
            Log.d(TAG, "setupUser: $token")
            if (!it.isLogin) {
                moveActivity()
            } else {
                setupData()
            }
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
        finish()
    }

    private fun setupAdapter() {
        listStoryAdapter = ListStoryAdapter()
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = listStoryAdapter
        }
    }

    private fun setupData() {
        showLoading()
        mainViewModel.getSession().observe(this@MainActivity) {
            token = "Bearer" + it.token
            Log.d(TAG, "setupUser: $token")
            Log.d(TAG, "setupName: ${it.email}")
        }

        mainViewModel.getStories().observe(this@MainActivity) {
            if (it != null) {
                listStoryAdapter.submitList(it)
            } else {
                Log.d(TAG, "setupData: null")
            }
        }
    }

    private fun setupAction() {
        binding.uploadStoryFab.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadActivity::class.java))
        }
    }

    private fun showLoading() {
        mainViewModel.isLoading.observe(this@MainActivity) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

}