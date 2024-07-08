package com.application.paymate

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.application.paymate.databinding.ActivityUserLoginBinding
import com.google.firebase.database.FirebaseDatabase

class UserLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserLoginBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var signingInDialog: SigningInDialog
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_login)
        fragmentManager = supportFragmentManager
        signingInDialog = SigningInDialog()

        sharedPreferences = getSharedPreferences("com.application.paymate", MODE_PRIVATE)

        val isLoggedIn = sharedPreferences.getBoolean("mate_loggedIn", false)
        if (isLoggedIn) {
            startActivity(Intent(this, MateDashboardActivity::class.java))
        }

        binding.loginButton.setOnClickListener {

            if (LoginInputsEmptyChecker.emptyOrNot(
                    binding.enterAdminUidEditText,
                    binding.enterMateUidEditText
                )
            ) {

                signingInDialog.show(fragmentManager, "loading")
                if (NetworkUtil.isNetworkAvailable(this)) {
                    HasInternetAccess.hasInternetAccess(object : HasInternetAccessCallback {
                        override fun onInternetAvailable() {
                            loginUser()
                        }

                        override fun onInternetNotAvailable() {
                            runOnUiThread {
                                Toast.makeText(
                                    this@UserLoginActivity,
                                    "Connection Timeout",
                                    Toast.LENGTH_SHORT
                                ).show()
                                signingInDialog.dismiss()
                            }
                        }
                    })
                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
                    signingInDialog.dismiss()
                }


            } else {
                Toast.makeText(this, "Please fill all the field first", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loginUser() {

        val enteredMateUid = binding.enterMateUidEditText.text.toString().trim()
        val enteredAdminUid = binding.enterAdminUidEditText.text.toString().trim()
        val matePath = "Mate: $enteredMateUid"
        val database = FirebaseDatabase.getInstance()
        val databaseReference =
            database.getReference("admin_profiles").child(enteredAdminUid).child("Mates")
                .child(matePath)
        databaseReference.child("mate_id").get().addOnSuccessListener {
            Toast.makeText(this, enteredAdminUid, Toast.LENGTH_SHORT).show()
            val id = it.value.toString()
            if (enteredMateUid == id) {
                sharedPreferences.edit().putString("id", id).apply()
                sharedPreferences.edit().putString("uid", enteredAdminUid).apply()
                sharedPreferences.edit().putBoolean("mate_loggedIn", true).apply()
                val getData = GetDataFromDatabase(databaseReference, sharedPreferences)
                getData.getAllData()
                signingInDialog.dismiss()
                startActivity(Intent(this, MateDashboardActivity::class.java))
            } else {
                Toast.makeText(this, "Not Correct", Toast.LENGTH_SHORT).show()
                signingInDialog.dismiss()
            }
        }
        databaseReference.get().addOnFailureListener {
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            signingInDialog.dismiss()
        }
    }
}