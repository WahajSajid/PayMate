package com.application.paymate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.application.paymate.databinding.ActivityAdmin2Binding
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityAdmin2Binding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val shareViewModel: SharedViewModel by viewModels()


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Getting data from Admin Login Activity
        val adminName = intent.getStringExtra("adminName")
        shareViewModel.adminName.value = adminName
        val adminEmail = intent.getStringExtra("adminEmail")
        shareViewModel.adminEmail.value = adminEmail


        //Setting Up logic for navigation drawer
        binding = ActivityAdmin2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolBar = binding.toolbar
        setSupportActionBar(toolBar)
        drawerLayout = findViewById(R.id.nav_drawer_header)
        //Setting up name of admin on runtime according to the data in the navigation drawer.
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
       val headerView =  navigationView.getHeaderView(0)
        val adminNameText = headerView.findViewById<TextView>(R.id.navAdminName)
        val adminEmailText = headerView.findViewById<TextView>(R.id.navAdminEmail)
        adminNameText.text = adminName
        adminEmailText.text = adminEmail

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

        //Setting up SetOnClick listener for add mate floating button
        binding.addButton.setOnClickListener { findNavController(R.id.NavigationHost).navigate(R.id.action_adminDashboard2_to_mateInfo) }


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