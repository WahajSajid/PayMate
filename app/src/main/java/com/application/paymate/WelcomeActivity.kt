package com.application.paymate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        //Setting up SharedPreferences to check the user is already installed application or not to show welcome screen.
        val sharedPreferences = getSharedPreferences("com.application.paymate", Context.MODE_PRIVATE)
        if(sharedPreferences.getBoolean("isInstalled",false)){
            val intent = Intent(this,AdminActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.adminButton.setOnClickListener {
            sharedPreferences.edit()?.putBoolean("isInstalled",true)?.apply()
            val intent = Intent(this,AdminLoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}