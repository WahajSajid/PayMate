package com.application.paymate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.ActivityAdminLoginBinding

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    val shareViewModel:LoginShareViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_admin_login)



        val sharedPreferences = getSharedPreferences("com.application.paymate", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if(isLoggedIn){
            startActivity(Intent(this, AdminActivity::class.java))
            finish()
        }

    }
}