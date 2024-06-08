package com.application.paymate

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.ActivityUserLoginBinding

class UserLoginActivity : AppCompatActivity() {
   private lateinit var binding: ActivityUserLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


       binding = DataBindingUtil.setContentView(this,R.layout.activity_user_login)

    }
}