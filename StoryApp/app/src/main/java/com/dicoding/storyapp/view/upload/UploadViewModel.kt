package com.dicoding.storyapp.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.remote.response.UploadStoryResponse
import com.dicoding.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.File

class UploadViewModel(private val repository: UserRepository) : ViewModel() {

    val isLoading: LiveData<Boolean> = repository.isLoading
    val uploadStoryResponse: LiveData<UploadStoryResponse> = repository.uploadStoryResponse

    fun uploadStory(token: String, imageFile: File, description: String) {
        viewModelScope.launch {
            repository.uploadStory(token, imageFile, description)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}