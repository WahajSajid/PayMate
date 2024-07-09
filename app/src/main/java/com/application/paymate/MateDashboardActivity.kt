package com.application.paymate

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.application.paymate.databinding.ActivityMateDasboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class MateDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMateDasboardBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var loadingData: LoadingDialogFragment
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: FirebaseDatabase

    @SuppressLint("UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mate_dasboard)


        val toolbar = binding.toolbar
        toolbar.title = "PayMate"

        sharedPreferences = getSharedPreferences("com.application.paymate", MODE_PRIVATE)
        database = FirebaseDatabase.getInstance()
        fragmentManager = supportFragmentManager
        loadingData = LoadingDialogFragment()
        loadingData.show(fragmentManager, "loading")

        //Getting id and adminUid from sharedPreferences
        val id = sharedPreferences.getString("id", "null").toString()
        val adminUid = sharedPreferences.getString("uid", "null").toString()
        val matePath = "Mate: $id"
        //Showing existing data initially.
        showExistingData()

        mateAvailableOrNot(adminUid,matePath)

//        if (NetworkUtil.isNetworkAvailable(this)) {
//            HasInternetAccess.hasInternetAccess(object : HasInternetAccessCallback {
//                override fun onInternetAvailable() {
//                    loadData(adminUid, matePath)
//                }
//
//                override fun onInternetNotAvailable() {
//                    runOnUiThread {
//                        Toast.makeText(
//                            this@MateDashboardActivity,
//                            "Connection Timeout",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        loadingData.dismiss()
//                        showExistingData()
//                    }
//                }
//            })
//        } else {
//            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
//            loadingData.dismiss()
//        }



        binding.refreshButton.setOnClickListener {
            loadingData.show(fragmentManager, "loading")
            loadData(adminUid, matePath)
        }


    }

    //Function to show existing data if due to network issue data not loaded from the firebase.
    private fun showExistingData() {
        binding.mateName.text = sharedPreferences.getString("name", "null").toString()
        binding.wallet.text = sharedPreferences.getString("wallet_amount", "0").toString()
        binding.rent.text = sharedPreferences.getString("rent_amount", "0").toString()
        binding.otherAmount.text = sharedPreferences.getString("other_amount", "0").toString()
        binding.id.text = sharedPreferences.getString("id", "null").toString()
    }


    private fun loadData(adminUid: String, matePath: String) {

        val databaseReference =
            database.getReference("admin_profiles").child(adminUid).child("Mates").child(matePath)
        getData(databaseReference.child("name"), "name", binding.mateName)
        getData(databaseReference.child("wallet_amount"), "wallet_amount", binding.wallet)
        getData(databaseReference.child("rent_amount"), "rent_amount", binding.rent)
        getData(databaseReference.child("other_amount"), "other_amount", binding.otherAmount)
        getData(databaseReference.child("mate_id"), "id", binding.id)
    }

    private fun getData(
        reference: DatabaseReference,
        localDatabaseReference: String,
        text: TextView
    ) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.value.toString()
                text.text = data
                sharedPreferences.edit().putString(localDatabaseReference, data).apply()
                loadingData.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MateDashboardActivity, error.message, Toast.LENGTH_SHORT).show()
                loadingData.dismiss()
            }
        })
    }

    private fun mateAvailableOrNot(adminUid: String, matePath: String) {
        if (NetworkUtil.isNetworkAvailable(this)) {
            HasInternetAccess.hasInternetAccess(object : HasInternetAccessCallback {
                override fun onInternetAvailable() {
                    val databaseReference = database.getReference("admin_profiles")
                    databaseReference.child(adminUid)
                        .addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {

                                    if(snapshot.child("Mates").child(matePath).exists()){
                                        loadData(adminUid, matePath)
                                    } else{
                                        showDialogFragment(
                                            "We are sorry. The admin has removed you.",
                                            "Admin Removed You!"
                                        )
                                        loadingData.dismiss()
                                    }
                                } else {
                                    showDialogFragment(
                                        "We are sorry. The admin has deleted the account",
                                        "Account Deleted!"
                                    )
                                    loadingData.dismiss()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@MateDashboardActivity,error.message,Toast.LENGTH_SHORT).show()
                                loadingData.dismiss()
                            }
                        })


                }

                override fun onInternetNotAvailable() {
                    runOnUiThread {
                        Toast.makeText(
                            this@MateDashboardActivity,
                            "Connection Timeout",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingData.dismiss()
                        showExistingData()
                    }
                }
            })
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            loadingData.dismiss()
        }

    }

    private fun showDialogFragment(message: String, title: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Ok") { _, _ ->
                sharedPreferences.edit().putBoolean("mate_loggedIn", false).apply()
                startActivity(Intent(this, UserLoginActivity::class.java))
                finish()
            }
            .setCancelable(false)
            .show()
    }
}