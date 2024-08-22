package com.application.paymate

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.application.paymate.databinding.ActivitySettingsBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var deletingAccount: DeletingAccountDialog
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var app: App
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        app = application as App

        sharedPreferences = getSharedPreferences("com.application.paymate", MODE_PRIVATE)

        val toolBar = binding.toolbar
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        val adminName = intent.getStringExtra("adminName")
        binding.editName.setText(adminName)
        markCheckBox()
        binding.saveChangesButton.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(this)) {
                saveChanges()
            } else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
        }

        binding.uid.text = app.uid.toString()

        binding.copyButton.setOnClickListener {
            copyText()
        }

        binding.deleteAccountButton.setOnClickListener {
            showAlertDialog()
        }

    }

    private fun saveChanges() {
        val editedName = binding.editName.text.toString()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        databaseReference.child("name").setValue(editedName)
        Toast.makeText(this@SettingsActivity, "Changes Saved", Toast.LENGTH_SHORT).show()
        if (binding.checkBox.isChecked) {
            enableAsMate()
        } else {
            disableAsMate()
        }
    }

    private fun enableAsMate() {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate")
        databaseReference.child("enabled").setValue(true)
        databaseReference.child("rent_amount").setValue("0")
        databaseReference.child("other_amount").setValue("0")

    }

    private fun disableAsMate() {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate")
        databaseReference.child("enabled").setValue(false)
    }


    //Function to mark the check box as checked if the admin made changes before.
    @SuppressLint("SuspiciousIndentation")
    private fun markCheckBox() {
        EnableAsMateOrNot.enableOrNot(object : EnableOrNotCallBack {
            override fun whenEnable() {
                binding.checkBox.isChecked = true
                sharedPreferences.edit().putBoolean("as_mate_enabled", true).apply()
            }

            override fun whenDisable() {
                binding.checkBox.isChecked = false
                sharedPreferences.edit().putBoolean("as_mate_enabled", false).apply()
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirmation")
        alertDialog.setMessage("Are you sure you want to delete the account")
        alertDialog.setPositiveButton("OK") { _, _ ->
            deleteAccount()
        }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun deleteAccount() {
        val sharedPreferences = getSharedPreferences("com.application.paymate", MODE_PRIVATE)
        val password = sharedPreferences.getString("password", "0")
        fragmentManager = supportFragmentManager
        deletingAccount = DeletingAccountDialog()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        deletingAccount.show(fragmentManager, "deleting_account")
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(user?.email!!, password.toString())
        user.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                user.delete()
                databaseReference.removeValue()
                sharedPreferences.edit().clear().apply()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Some Error Occurs", Toast.LENGTH_SHORT).show()
                deletingAccount.dismiss()
            }
        }
    }

    //Function to copy the text to clipboard
    private fun copyText() {
        val text = binding.uid.text.toString()
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "uid copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}