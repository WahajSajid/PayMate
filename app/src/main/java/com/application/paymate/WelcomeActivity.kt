package com.application.paymate

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWelcomeBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)


        //Setting up SharedPreferences to check the user is already installed application or not to show welcome screen.
        val sharedPreferences = getSharedPreferences("com.application.paymate", Context.MODE_PRIVATE)


        val isInstalledAndAdmin = sharedPreferences.getBoolean("isInstalledAndAdmin",false)
        val isInstalledAndMate = sharedPreferences.getBoolean("isInstalledAndMate" ,false)

        if(isInstalledAndAdmin){
            val intent = Intent(this,AdminLoginActivity::class.java)
            startActivity(intent)
            finish()
        } else if(isInstalledAndMate) {
                val intent = Intent(this, UserLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            binding.adminButton.setOnClickListener {
                sharedPreferences.edit()?.putBoolean("isInstalledAndAdmin", true)?.apply()
                val intent = Intent(this, AdminLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            binding.userButton.setOnClickListener {
                sharedPreferences.edit()?.putBoolean("isInstalledAndMate", true)?.apply()
                val intent = Intent(this, UserLoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
}