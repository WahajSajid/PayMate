package com.application.paymate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.ActivityFrontScreenBinding

class FrontScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrontScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_front_screen)
    }
}