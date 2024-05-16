package com.dicoding.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ItemStoryBinding
import com.dicoding.storyapp.view.detail.DetailActivity

class ListStoryAdapter : ListAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var binding:ItemStoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bindingItem(story)
        }
    }

    class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindingItem(listStory: ListStoryItem) {
            binding.apply {
                titleStory.text = listStory.name
                descStory.text = listStory.description
                Glide.with(itemView.context)
                    .load(listStory.photoUrl)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageStory)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("name", listStory.name)
                    intent.putExtra("description", listStory.description)
                    intent.putExtra("photo", listStory.photoUrl)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.name == newItem.name && oldItem.description == newItem.description && oldItem.photoUrl == newItem.photoUrl
            }
        }
    }


}