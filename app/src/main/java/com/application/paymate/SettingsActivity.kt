package com.application.paymate

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.ActivitySettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("DEPRECATION")
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        val toolBar = binding.toolbar
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        val adminName = intent.getStringExtra("adminName")
        binding.editName.setText(adminName)
        markCheckBox()
        Toast.makeText(this,adminName,Toast.LENGTH_SHORT).show()
        binding.saveChangesButton.setOnClickListener {
            saveChanges()
        }


    }

    private fun saveChanges() {
       val editedName =  binding.editName.text.toString()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        databaseReference.child("name").setValue(editedName)
        if(binding.checkBox.isChecked){
            enableAsMate()
        } else{
            disableAsMate()
        }
    }
    private fun enableAsMate(){
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate")
        databaseReference.child("enabled").setValue(true)
        databaseReference.child("rent").setValue("")
        databaseReference.child("other_dues").setValue("")
    }

    private fun disableAsMate(){
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate")
        databaseReference.child("enabled").setValue(false)
    }


    //Function to mark the check box as checked if the admin made changes before.
   private fun markCheckBox(){
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate").child("enabled")
            databaseReference.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                   if(snapshot.value == true) binding.checkBox.isChecked = true
                    else binding.checkBox.isChecked = false
                }

                override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(this@SettingsActivity,error.message,Toast.LENGTH_SHORT).show()
                }
            })
   }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}