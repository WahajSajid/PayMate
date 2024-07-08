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
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminLoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var signingInDialog: SigningInDialog
    private lateinit var fragmentManager: FragmentManager

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_screen, container, false)

        signingInDialog = SigningInDialog()
        fragmentManager = childFragmentManager

        //Initializing Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()


        //Setting up click listener on Login Button
        binding.loginButton.setOnClickListener {

            if (LoginInputsEmptyChecker.emptyOrNot(
                    binding.enterEmailEditText,
                    binding.passwordEditText
                )
            ) {
                signingInDialog.show(fragmentManager, "loading")
                if (NetworkUtil.isNetworkAvailable(requireContext())) {
                    HasInternetAccess.hasInternetAccess(object : HasInternetAccessCallback {
                        override fun onInternetAvailable() {
                            val email: String = binding.enterEmailEditText.text.toString()
                            val password: String = binding.passwordEditText.text.toString()
                            signInUser(email, password)
                        }

                        override fun onInternetNotAvailable() {
                            requireActivity().runOnUiThread {
                                Toast.makeText(context, "Connection Timeout", Toast.LENGTH_SHORT)
                                    .show()
                                signingInDialog.dismiss()
                            }
                        }
                    })
                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                    signingInDialog.dismiss()
                }
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

    //Function to sign user
    @SuppressLint("CommitPrefEdits")
    private fun signInUser(email: String, password: String) {
        val sharedPreferences =
            activity?.getSharedPreferences("com.application.paymate", Context.MODE_PRIVATE)
        var isLoggedIn: Boolean
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    reference.child("admin_profiles").child(firebaseAuth.uid.toString()).get()
                        .addOnSuccessListener { snapshot ->
                            val adminName = snapshot.child("name").value.toString()
                            val adminEmail = snapshot.child("email").value.toString()
                            val intent = Intent(context, AdminActivity::class.java)
                            isLoggedIn = true
                            sharedPreferences?.edit()?.putBoolean("isLoggedIn", isLoggedIn)?.apply()
                            sharedPreferences?.edit()?.putString("adminName", adminName)?.apply()
                            sharedPreferences?.edit()?.putString("adminEmail", adminEmail)?.apply()
                            sharedPreferences?.edit()?.putString("password", password)?.apply()
                            signingInDialog.dismiss()
                            startActivity(intent)
                            activity?.finish()
                        }
                } else {
                    signingInDialog.dismiss()
                    Toast.makeText(context, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}