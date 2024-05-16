package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.StoryResponse
import com.dicoding.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = repository.isLoading
//    val getStories: LiveData<List<ListStoryItem>> = repository.getStories("token")

    fun getStories(): LiveData<List<ListStoryItem>> {
        repository.getStories()
        return repository.storyResponse
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}