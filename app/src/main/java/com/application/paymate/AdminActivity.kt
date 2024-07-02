package com.application.paymate

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.application.paymate.databinding.ActivityAdmin2Binding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION", "CAST_NEVER_SUCCEEDS", "NAME_SHADOWING")
class AdminActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityAdmin2Binding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val shareViewModel: SharedViewModel by viewModels()
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmin2Binding.inflate(layoutInflater)
        //Setting Up logic for navigation drawer
        setContentView(binding.root)
        val toolBar = binding.toolbar
        setSupportActionBar(toolBar)
        drawerLayout = findViewById(R.id.nav_drawer_header)


        //Updating UI of navigation drawer header with admin details
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val adminNameText = headerView.findViewById<TextView>(R.id.navAdminName)
        val adminEmailText = headerView.findViewById<TextView>(R.id.navAdminEmail)
        val sharedPreferences =
            getSharedPreferences("com.application.paymate", Context.MODE_PRIVATE)
        val adminEmail = sharedPreferences.getString("adminEmail", "Loading...")


        getName(adminNameText)
        adminEmailText.text = adminEmail

        //Setting up logic for navigation drawer items clicked.
        navigationView.setNavigationItemSelectedListener { item -> // Handle item selection here
            when (item.itemId) {
                R.id.nav_settings -> {
                    val adminName = adminNameText.text.toString()
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("adminName", adminName)
                    startActivity(intent)
                }

                R.id.nav_about -> {

                }

                R.id.nav_logout -> {
                    logOut()
                }
                // Add other cases for remaining menu items
            }
            false
        }
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

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun logOut() {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val loginDetail = getSharedPreferences(
            "com.application.paymate",
            MODE_PRIVATE
        )
        loginDetail?.edit()?.putBoolean("isLoggedIn", false)?.apply()
        startActivity(Intent(this, AdminLoginActivity::class.java))
        finish()
    }

    fun getName(adminNameTextView: TextView){
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("name")
        databaseReference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                adminNameTextView.text = snapshot.value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}