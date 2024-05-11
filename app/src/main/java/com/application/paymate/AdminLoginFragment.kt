package com.application.paymate

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminLoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)


            //Initializing Firebase Auth
            firebaseAuth = FirebaseAuth.getInstance()


            //Setting up click listener on Login Button
            binding.loginButton.setOnClickListener {
                if (emptyOrNot()) {
                    val email: String = binding.enterEmailEditText.text.toString()
                    val password: String = binding.passwordEditText.text.toString()
                    signInUser(email, password)
                } else {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }
            }

            binding.registerTextButton.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_adminLoginFragment_to_adminRegistrationFragment)
            }
        return binding.root
    }

    //Function to check if the input fields are empty or not.
    private fun emptyOrNot(): Boolean {
        var isTrueOrFalse = false
        if (binding.enterEmailEditText.text.toString()
                .isEmpty() || binding.passwordEditText.text.toString().isEmpty()
        ) {
            isTrueOrFalse = false
        } else isTrueOrFalse = true
        return isTrueOrFalse
    }


    //Function to sign user
    @SuppressLint("CommitPrefEdits")
    private fun signInUser(email: String, password: String) {
        val sharedPreferences = activity?.getSharedPreferences("com.application.paymate", Context.MODE_PRIVATE)
        var isLoggedIn:Boolean
        binding.spinnerLayout.visibility = View.VISIBLE
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.child("admin_profiles").child(firebaseAuth.uid.toString()).get()
                        .addOnSuccessListener { snapshot ->
                           val adminName =  snapshot.child("name").value.toString()
                            val adminEmail = snapshot.child("email").value.toString()
                            val intent = Intent(context,AdminActivity::class.java)
                            isLoggedIn = true
                            sharedPreferences?.edit()?.putBoolean("isLoggedIn",isLoggedIn)?.apply()
                            sharedPreferences?.edit()?.putString("adminName",adminName)?.apply()
                            sharedPreferences?.edit()?.putString("adminEmail",adminEmail)?.apply()
                            startActivity(intent)
                            activity?.finish()
                        }
                } else {
                    binding.spinnerLayout.visibility = View.GONE
                    Toast.makeText(context, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}