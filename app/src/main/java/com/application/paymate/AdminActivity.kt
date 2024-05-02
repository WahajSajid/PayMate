package com.application.paymate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.application.paymate.databinding.ActivityAdmin2Binding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityAdmin2Binding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdmin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolBar = binding.toolbar
        setSupportActionBar(toolBar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                // Handle item selection here
                when (item.itemId) {
                    R.id.nav_home -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AdminDashboard()).commit()
                        return true
                    }
                    R.id.nav_settings -> {
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SettingsFragment()).commit()
                        return true
                    }

                    R.id.nav_about -> {
//                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AboutFragment()).commit()
                        return true
                    }

                    R.id.nav_logout -> {
                    }
                    // Add other cases for remaining menu items
                }
                return false
            }

        })
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_nav,
            R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AdminDashboard()).commit()
            navigationView.setCheckedItem(R.id.nav_home)
        }

        //Setting Up logic for bottom Navigation View and ToolBar
        val bottomBar: BottomNavigationView = binding.bottomNavigationView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.NavigationHost) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            fallbackOnNavigateUpListener = ::onSupportNavigateUp,
            topLevelDestinationIds = (setOf(R.id.adminDashboard2))
        )
        bottomBar.setupWithNavController(navController)
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