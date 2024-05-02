package com.application.paymate

import android.annotation.SuppressLint
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.application.paymate.R.color.white
import com.application.paymate.databinding.ActivityAdmin2Binding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdmin2Binding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdmin2Binding.inflate(layoutInflater)
        setContentView(binding.root)



        //Setting Up logic for bottom Navigation View and ToolBar
        val bottomBar: BottomNavigationView = binding.bottomNavigationView
        setSupportActionBar(binding.myToolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.NavigationHost) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            fallbackOnNavigateUpListener = ::onSupportNavigateUp,
            topLevelDestinationIds = (setOf(R.id.adminDashboard2))
        )
        binding.myToolbar.setupWithNavController(navController, appBarConfiguration)
        bottomBar.setupWithNavController(navController)
    }
}