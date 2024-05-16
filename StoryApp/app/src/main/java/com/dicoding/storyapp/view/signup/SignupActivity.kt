package com.dicoding.storyapp.view.signup

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
import com.dicoding.storyapp.databinding.ActivitySignupBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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

    private fun  setupAction() {
        binding.signupButton.setOnClickListener {
            showLoading()
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            signupViewModel.register(name, email, password)
            signupViewModel.toastText.observe(this@SignupActivity) {
                it.getContentIfNotHandled()?.let { toastText ->
                    Toast.makeText(
                        this@SignupActivity, toastText, Toast.LENGTH_SHORT
                    ).show()
                }
            }

            signupViewModel.registerResponse.observe(this@SignupActivity) { response ->
                if (response.error == false) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Success!")
                        setMessage("Congratulations, your account $email has been successfully created")
                        setPositiveButton("Continue") { _, _ ->
                            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
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
        val signupButton = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        val labelName = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(300)
        val labelEmail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val labelPassword = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val layoutName = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val layoutEmail = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val layoutPassword = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)

        val togetherLayout = AnimatorSet().apply {
            playTogether(labelName, labelEmail, labelPassword, layoutName, layoutEmail, layoutPassword)
        }

        AnimatorSet().apply {
            playSequentially(title, togetherLayout, signupButton)
            start()
        }
    }

    private fun showLoading() {
        signupViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }
}