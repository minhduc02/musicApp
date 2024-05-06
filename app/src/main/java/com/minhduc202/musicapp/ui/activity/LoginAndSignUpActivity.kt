package com.minhduc202.musicapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.minhduc202.musicapp.base.BaseActivity
import com.minhduc202.musicapp.constant.Constants
import com.minhduc202.musicapp.R
import com.minhduc202.musicapp.databinding.ActivityLoginAndSignUpBinding
import com.minhduc202.musicapp.ui.fragment.LoginFragment
import com.minhduc202.musicapp.ui.fragment.SignUpFragment

class LoginAndSignUpActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginAndSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAndSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        if (intent?.action == Constants.ACTION_LOG_IN) replaceFragment(LoginFragment.newInstance())
        else replaceFragment(SignUpFragment.newInstance())
    }

    fun goToLogin() {
        replaceFragment(LoginFragment.newInstance())
    }

    fun goToSignUp() {
        replaceFragment(SignUpFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}