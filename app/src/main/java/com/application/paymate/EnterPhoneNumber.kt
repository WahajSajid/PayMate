package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.application.paymate.databinding.FragmentEnterPhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class EnterPhoneNumber : Fragment() {
    private lateinit var binding: FragmentEnterPhoneNumberBinding
    val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_enter_phone_number, container, false
        )
        val auth = FirebaseAuth.getInstance()
        binding.sendOTPButton.setOnClickListener {
            if (inputFieldEmptyOrnNot()) sendCode(auth)
            else Toast.makeText(context, "Enter phone number first", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    //Function to check if the input field is empty or not
    private fun inputFieldEmptyOrnNot(): Boolean {
        var isTrueOrFalse = false
        if (binding.enterPhoneEditText.text.toString().isNotEmpty()) isTrueOrFalse = true
        return isTrueOrFalse
    }

    //Function to send code
    private fun sendCode(auth: FirebaseAuth) {

        //Setting up callBack
        val callBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signInWithCredential(p0,auth)

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {

            }
        }


        val phoneNumber = binding.enterPhoneEditText.text.toString()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callBack)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    //Function to sign in mate
    private fun signInWithCredential(credential: PhoneAuthCredential,auth: FirebaseAuth){
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) Toast.makeText(context,"Signed in successfully",Toast.LENGTH_SHORT).show()
                else Toast.makeText(context,"Failed to sign in",Toast.LENGTH_SHORT).show()
            }
    }

}