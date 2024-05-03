package com.application.paymate

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowInsetsController
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.application.paymate.databinding.ActivityAdmin2Binding
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityAdmin2Binding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting Up logic for navigation drawer
        binding = ActivityAdmin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolBar = binding.toolbar
        setSupportActionBar(toolBar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                // Handle item selection here
                when (item.itemId) {
                    R.id.nav_settings -> {
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, AdminProfile()).commit()
//                        drawerLayout.closeDrawer(GravityCompat.START)
                        return true
                    }

                    R.id.nav_about -> {

                        return true
                    }

                    R.id.nav_logout -> {
                    }
                    // Add other cases for remaining menu items
                }
                return false
            }

        })
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolBar, R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, AdminDashboard()).commit()
////            navigationView.setCheckedItem(R.id.nav_dasboard)
//        }

        //Setting Up logic for BottomBar
        val bottomBar = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.NavigationHost) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            fallbackOnNavigateUpListener = ::onSupportNavigateUp,
            topLevelDestinationIds = (setOf(R.id.adminDashboard2))
        )
        bottomBar.setupWithNavController(navController)

        //Setting up logic for status bar color



    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}