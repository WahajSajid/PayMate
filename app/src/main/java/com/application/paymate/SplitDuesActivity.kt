package com.application.paymate

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
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
import kotlinx.coroutines.sync.Mutex

@Suppress("DEPRECATION")
class SplitDuesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplitDuesBinding
    private lateinit var matesList: ArrayList<MatesInfo>
    private lateinit var mateIds: ArrayList<String>
    private lateinit var mateName: ArrayList<String>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_split_dues)

        //Counter for the number of mates selected

        //Creating an instance of UpdateOrSplitDues class to split the dues
        val splitDuesObject = UpdateOrSplitDues("")

        val toolbar = binding.myToolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Split Dues"

        //Initializing matesList
        matesList = ArrayList()

        //Initializing mateIds and mateNames
        mateIds = ArrayList()
        mateName = ArrayList()


//        Setting up adapter for recycler View
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = SplitDuesAdapter(matesList, this)
        recyclerView.adapter = adapter
        adapter.itemClickListener(object : SplitDuesAdapter.OnItemClickListener {
            override fun checkBoxClickListener(id: TextView, checkBox: CheckBox) {
                //Adding the selected mate id the mateId array list to split the dues
                if (checkBox.isChecked) {
                    mateIds.add(id.text.toString())
                    mateName.add(checkBox.text.toString())
                } else {
                    mateIds.remove(id.text.toString())
                    mateName.remove(checkBox.text.toString())
                }
            }

            override val mutex: Mutex = Mutex()
        })


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
        }


        //Calling a function to divide the dues

        //Setting up adapter for drop down
        val dropDown = binding.selectOptionDropDown
        val dropDownItems = arrayOf("Other Dues", "Rent")
        val dropDownAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, dropDownItems)
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDown.adapter = dropDownAdapter

        var isSelected = false

        //Setting up onClick listener for select all button
        binding.selectAllButton.setOnClickListener {
            isSelected = !isSelected
          val allMateIds =  adapter.selectAllMates(isSelected)
            if (isSelected) {
                mateIds.addAll(allMateIds)
                binding.selectAllButton.text = "Unselect All"
            }
            if (!isSelected) {
                mateIds.clear()
                allMateIds.clear()
                binding.selectAllButton.text = "Select All"
            }
        }

        //Setting up the logic to implement the on item selected for drop down items
        dropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
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
                                this@SplitDuesActivity, it, mateIds
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
                                this@SplitDuesActivity, it, mateIds
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
        onBackPressed()
        return true
    }
}