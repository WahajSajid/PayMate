package com.application.paymate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.application.paymate.databinding.ActivityAllMatesBinding

@Suppress("DEPRECATION")
class AllMatesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllMatesBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_mates)
        val toolbar = binding.myToolbar
        setSupportActionBar(toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.NavigationHost) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            fallbackOnNavigateUpListener = ::onSupportNavigateUp,
            topLevelDestinationIds = (setOf(R.id.allMates2))
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = destination.label
        }
        setupBackButton()

    }

    //Method to set up the action bar with back stack of activity and fragments
    private fun setupBackButton() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.setDisplayHomeAsUpEnabled(destination.id == R.id.allMates2 || destination.id == R.id.editMateDetailsFragment || destination.id == R.id.updateFragment2 || destination.id == R.id.rentUpdateFragment2 || destination.id == R.id.otherDueUpdateFragment2 || destination.id == R.id.walletUpdateFragment4)
            supportActionBar?.title = destination.label
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}