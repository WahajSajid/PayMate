package com.application.paymate

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.paymate.databinding.ActivitySplitDuesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION")
class SplitDuesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplitDuesBinding
    private lateinit var matesList: ArrayList<MatesInfo>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var myApp: App

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_split_dues)

        //Creating an instance of App class to get the data
        myApp = application as App
        myApp.ids.clear()
        sharedPreferences = getSharedPreferences("com.application.paymate", MODE_PRIVATE)
        val enabledAsMate = sharedPreferences.getBoolean("as_mate_enabled", false)
        matesList = ArrayList()
        val adapter = SplitDuesAdapter(matesList, this, myApp)
        binding.refreshButtonSplitActivity.setOnClickListener {
            binding.noInternetConnectionIconLayout.visibility = View.GONE
            if (NetworkUtil.isNetworkAvailable(this)) {
                val showCard = ShowAdminCard()
                showCard.showAdminCard(binding.checkBox)
            }
            showRecyclerView(enabledAsMate, adapter)
        }

        //Creating an instance of UpdateOrSplitDues class to split the dues
        val splitDuesObject = UpdateOrSplitDues("")

        val toolbar = binding.myToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Split Dues"

        showRecyclerView(enabledAsMate, adapter)

        if (NetworkUtil.isNetworkAvailable(this)) {
            val showCard = ShowAdminCard()
            showCard.showAdminCard(binding.checkBox)
        }


        //Calling a function to divide the dues

        //Setting up adapter for drop down
        val dropDown = binding.selectOptionDropDown
        val dropDownItems = arrayOf("Other Dues", "Rent")
        val dropDownAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, dropDownItems)
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDown.adapter = dropDownAdapter


        //Setting up the logic to implement the on item selected for drop down items
        dropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (position) {
                    0 -> {
                        view?.let {
                            splitDuesObject.splitDues(
                                binding.splitButton,
                                binding.selectAllButton,
                                "update_other_amount",
                                "plus",
                                binding.enterAmountEditText,
                                this@SplitDuesActivity,
                                it,
                                myApp,
                                binding.checkBox,
                                enabledAsMate,
                                adapter
                            )
                        }
                    }

                    1 -> {
                        view?.let {
                            splitDuesObject.splitDues(
                                binding.splitButton,
                                binding.selectAllButton,
                                "update_rent",
                                "plus",
                                binding.enterAmountEditText,
                                this@SplitDuesActivity,
                                it,
                                myApp,
                                binding.checkBox,
                                enabledAsMate,
                                adapter
                            )
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        myApp.ids.clear()
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun showRecyclerView(enableAsMate: Boolean, adapter: SplitDuesAdapter) {
        //        Setting up adapter for recycler View
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        var isSelected = false
        binding.checkBox.isChecked = false
        //Setting up onClick listener for select all button
        binding.selectAllButton.setOnClickListener {
            if (isSelected) {
                isSelected = false
                if (enableAsMate) {
                    binding.checkBox.isChecked = isSelected
                }
                adapter.selectAllMates(isSelected)
                myApp.ids.clear()
                Toast.makeText(
                    this@SplitDuesActivity,
                    myApp.ids.size.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                binding.selectAllButton.text = "Select All"
            } else {
                isSelected = true
                if (enableAsMate) {
                    binding.checkBox.isChecked = isSelected
                }
                adapter.selectAllMates(isSelected)
                myApp.ids.clear()
                for (data in myApp.mateList) {
                    myApp.ids.add(data.mate_id!!)
                }
                Toast.makeText(
                    this@SplitDuesActivity,
                    myApp.ids.size.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                binding.selectAllButton.text = "Unselect All"
            }
        }


        //Creating instance of Firebase Database
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")

        binding.spinnerLayout.visibility = View.VISIBLE

        //Value Event listener to retrieve the data from firebase realtime database
        if (NetworkUtil.isNetworkAvailable(this)) {

            databaseReference.addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.spinnerLayout.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE


                    //Checking if any mate is exists in the database or not. If exists then show the list is empty message
                    if (!snapshot.exists()) {
                        binding.emptyListLayout.visibility = View.VISIBLE
                        binding.listOfMatesLayout.visibility = View.GONE
                    } else {
                        binding.emptyListLayout.visibility = View.GONE
                        binding.listOfMatesLayout.visibility = View.VISIBLE
                    }
//                        binding.listOfMatesLayout.visibility = View.VISIBLE
                    matesList.clear()
                    binding.spinnerLayout.visibility = View.GONE
                    for (data in snapshot.children) {
                        val mateInfo = data.getValue(MatesInfo::class.java)
                        matesList.add(mateInfo!!)
                    }
                    myApp.mateList = matesList
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SplitDuesActivity, error.message, Toast.LENGTH_SHORT).show()
                    binding.spinnerLayout.visibility = View.GONE
                }
            })

        } else {
            binding.spinnerLayout.visibility = View.GONE
            binding.noInternetConnectionIconLayout.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        }

    }

}