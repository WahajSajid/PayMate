package com.application.paymate

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.ActivityUserLoginBinding
import com.google.firebase.database.FirebaseDatabase

class UserLoginActivity : AppCompatActivity() {
   private lateinit var binding: ActivityUserLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_user_login)
        binding.loginButton.setOnClickListener {
            if(LoginInputsEmptyChecker.emptyOrNot(binding.enterMateUidEditText,binding.enterAdminUidEditText)){
                loginUser()
            } else Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_SHORT).show()
        }

    }

    private fun loginUser(){
        val enteredMateUid = binding.enterMateUidEditText.text.toString().trim()
        val enteredAdminUid = binding.enterAdminUidEditText.text.toString().trim()

        val matePath = "Mate: $enteredMateUid"
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles").child(enteredAdminUid).child("Mates").child(matePath).child("mate_id")
        databaseReference.get().addOnSuccessListener {
            Toast.makeText(this,enteredAdminUid,Toast.LENGTH_SHORT).show()
            val id = it.value.toString()
            if(enteredMateUid == id) Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show()
            else Toast.makeText(this,"Not Correct",Toast.LENGTH_SHORT).show()
        }
        databaseReference.get().addOnFailureListener {
            Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
        }
    }
}