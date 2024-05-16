package com.dicoding.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            showLoading()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginViewModel.login(email, password)
            loginViewModel.loginResponse.observe(this@LoginActivity) { response ->
                loginViewModel.saveSession(
                    UserModel(
                        response.loginResult?.name.toString(),
                        response.loginResult?.token.toString(),
                        true
                    )
                )
            }

            loginViewModel.toastText.observe(this@LoginActivity) {
                it.getContentIfNotHandled()?.let { toastText ->
                    Toast.makeText(
                        this@LoginActivity, toastText, Toast.LENGTH_SHORT
                    ).show()
                }
            }

            loginViewModel.loginResponse.observe(this@LoginActivity) { response ->
                if (response.error == false) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Success!")
                        setMessage("Login successful! Let's post your stories")
                        setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(1000)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(300)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)

        val labelEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val labelPassword = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val layoutEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val layoutPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)

        val togetherLayout = AnimatorSet().apply {
            playTogether(labelEmail, labelPassword, layoutEmail, layoutPassword)
        }

        val together = AnimatorSet().apply {
            playTogether(message, loginButton)
        }

        AnimatorSet().apply {
            playSequentially(title, togetherLayout, together)
            start()
        }
    }

    private fun showLoading() {
        loginViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}