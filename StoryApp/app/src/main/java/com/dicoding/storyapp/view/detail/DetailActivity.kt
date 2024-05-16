package com.dicoding.storyapp.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupDetailStory()
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_detail_story)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setupDetailStory() {
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val photo = intent.getStringExtra("photo")

        binding.apply {
            titleDetailStory.text = name
            descDetailStory.text = description
            Glide.with(this@DetailActivity)
                .load(photo)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageDetailStory)
        }
    }
}