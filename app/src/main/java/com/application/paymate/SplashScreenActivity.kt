package com.application.paymate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.ActivitySplashScreenBinding
import com.google.android.material.appbar.AppBarLayout

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen)



        AppBarLayout.GONE
            Handler(mainLooper).postDelayed({
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }, 1000)
    }
}