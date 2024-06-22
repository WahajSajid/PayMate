package com.application.paymate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.paymate.databinding.ActivitySplitDuesBinding
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("DEPRECATION")
class SplitDuesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplitDuesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_split_dues)

        val toolbar = binding.myToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Split Dues"


        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        //Creating object of FragmentStateAdapter to handle fragments
        viewPager.adapter = object : FragmentStateAdapter(this) {

            //Number of tabs for fragments
            override fun getItemCount(): Int = 2

            //Handling fragments according to tab positions
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> SplitOtherDuesFragment()
                    1 -> SplitRentFragment()
                    else -> Fragment()
                }
            }
        }
        //Attaching tab layout with view pager on run time
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Other Dues"
                1 -> "Rent"
                else -> ""
            }
        }.attach()


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}