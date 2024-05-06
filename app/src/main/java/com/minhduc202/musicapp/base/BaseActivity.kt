package com.minhduc202.musicapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minhduc202.musicapp.util.LanguageUtil

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageUtil.setLanguage(this@BaseActivity)
    }
}