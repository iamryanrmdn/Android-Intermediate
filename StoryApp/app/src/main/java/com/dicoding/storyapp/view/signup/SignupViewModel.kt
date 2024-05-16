package com.dicoding.storyapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.remote.response.RegisterResponse
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.di.Event
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    val registerResponse: LiveData<RegisterResponse> = repository.registerResponse
    val isLoading: LiveData<Boolean> = repository.isLoading
    val toastText: LiveData<Event<String>> = repository.toastText

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.register(name, email, password)
        }
    }
}