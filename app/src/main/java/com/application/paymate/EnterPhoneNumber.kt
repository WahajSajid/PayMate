package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.application.paymate.databinding.FragmentEnterPhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
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
        //Setting up logic for drop down menu
        val countryCode = arrayOf("+92")
        val adapter = ArrayAdapter(this.requireContext(),android.R.layout.simple_spinner_item,countryCode)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countryCodeDropDown.adapter = adapter
        val phoneNumber = binding.enterPhoneEditText.text?.trim().toString()
        val phone = "+92$phoneNumber"

        val auth = FirebaseAuth.getInstance()
        binding.sendOTPButton.setOnClickListener {
            if (inputFieldEmptyOrnNot())
                if(binding.enterPhoneEditText.text?.length == 10){
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L,TimeUnit.SECONDS)
                        .setActivity(this.requireActivity())
                        .setCallbacks(callbacks)
                }
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

   private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }



}