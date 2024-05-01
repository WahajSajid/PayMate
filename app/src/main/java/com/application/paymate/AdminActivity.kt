package com.application.paymate

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.application.paymate.databinding.ActivityAdmin2Binding

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdmin2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdmin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}