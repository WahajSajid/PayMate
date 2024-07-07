package com.application.paymate

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.application.paymate.databinding.ActivitySettingsBinding
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION")
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var fragmentManager:FragmentManager
    private lateinit var deletingAccount:DeletingAccountDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        val app = application as App

        val toolBar = binding.toolbar
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        val adminName = intent.getStringExtra("adminName")
        binding.editName.setText(adminName)
        markCheckBox()
        binding.saveChangesButton.setOnClickListener {
            if (NetworkUtil.isNetworkAvailable(this)) {
                saveChanges(app)
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

    private fun saveChanges(myApp: App) {
        val editedName = binding.editName.text.toString()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        databaseReference.child("name").setValue(editedName)
        Toast.makeText(this@SettingsActivity, "Changes Saved", Toast.LENGTH_SHORT).show()
        if (binding.checkBox.isChecked) {
            enableAsMate(myApp)
        } else {
            disableAsMate()
        }
    }

    private fun enableAsMate(myApp:App) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate")
        databaseReference.child("enabled").setValue(true)
        databaseReference.child("rent_amount").setValue("0")
        databaseReference.child("other_amount").setValue("0")
        myApp.enabled = true

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
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate").child("enabled")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) binding.checkBox.isChecked = true
                else binding.checkBox.isChecked = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SettingsActivity, error.message, Toast.LENGTH_SHORT).show()
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
        val password = sharedPreferences.getString("password","0")
        fragmentManager = supportFragmentManager
        deletingAccount = DeletingAccountDialog()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles").child(FirebaseAuth.getInstance().currentUser!!.uid)
        deletingAccount.show(fragmentManager,"deleting_account")
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(user?.email!!,password.toString())
        user.reauthenticate(credential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                user.delete()
                databaseReference.removeValue()
                sharedPreferences.edit()?.putString("password","0")?.apply()
                sharedPreferences.edit().putBoolean("isInstalledAndAdmin", false).apply()
                sharedPreferences.edit().putBoolean("isLoggedIn",false).apply()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                Toast.makeText(this,"Account deleted successfully",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Some Error Occurs",Toast.LENGTH_SHORT).show()
                deletingAccount.dismiss()
            }
        }
    }

    //Function to copy the text to clipboard
    private fun copyText(){
       val text =  binding.uid.text.toString()
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this,"uid copied to clipboard",Toast.LENGTH_SHORT).show()
    }
}