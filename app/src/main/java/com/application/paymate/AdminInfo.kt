package com.application.paymate

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentAdminInfoBinding
import com.google.firebase.auth.FirebaseAuth

class AdminInfo : Fragment() {
    private lateinit var binding: FragmentAdminInfoBinding
    private var password:String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var email:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_info, container, false)
        //Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()



        //Setting up minimum length for phone number and pin number
        val minPinLength = 6 // Minimum Pin length
        val pinLengthFilter = arrayOf<InputFilter>(InputFilter.LengthFilter(minPinLength))
        binding.confirmPasswordEditText.filters = pinLengthFilter

        //Setting up TextWatcher for phone number to check the length of number, if the length is 11 proceed ahead otherwise throw error.
        val emailValidator = EmailValidator(object : EmailValidatorCallBack {
            override fun onInputValidated(isValid: Boolean) {
                if (isValid) {
                    //FireBase code goes here to store user input
                    email = binding.enterEmailEditText.text.toString()
                } else {
                    binding.enterEmailEditText.error = "Invalid"
                }
            }
        })
        binding.enterEmailEditText.addTextChangedListener(emailValidator)

        //Setting up TextWatcher for username to check if the username is starting with admin or not and if there is any space in between.
        val usernameValidator = UsernameValidator(object : UsernameValidatorCallBack {
            override fun onInputValidated(isValid: Boolean) {
                if (isValid) {
                    //FireBase code goes here to store user input
                    name = binding.enterUserNameEditText.text.toString()
                } else {
                    binding.enterUserNameEditText.error = "Invalid"
                }
            }
        })
        binding.enterUserNameEditText.addTextChangedListener(usernameValidator)

        //Setting up TextWatcher for create pin EditText to check the length of pin, if the length is 4 proceed ahead otherwise throw error.
//        val createPinValidator = CreatePinValidator(object :CreaatePinValidatorCallBack{
//            override fun onInputValidated(isValid: Boolean) {
//                if(isValid){
//                    //FireBase code goes here to store user input
//                  password = binding.createPasswordEditText.text.toString()
//                } else{
//                    binding.createPasswordEditText.error = "Invalid"
//                }
//            }
//        })
//        binding.createPasswordEditText.addTextChangedListener(createPinValidator)

        //Setting up TextWatcher for confirm pin EditText to check the length of pin, if the length is 4 proceed ahead otherwise throw error.
        val confirmPinValidator = ConfirmPinValidator(object:ConfirmPinValidatorCallBack{
            override fun onInputValidated(isValid: Boolean) {
                if(isValid){
                    if(binding.confirmPasswordEditText.text.toString() == password){
                        //FireBase code goes here to store user input
                        password = binding.confirmPasswordEditText.text.toString()
                    } else binding.confirmPasswordEditText.error = "Create and confirm pin numbers does not match"
                } else{
                    binding.confirmPasswordEditText.error = "Invalid"
                }
            }
        })
        binding.confirmPasswordEditText.addTextChangedListener(confirmPinValidator)
        binding.registerButton.setOnClickListener {
            name = binding.enterNameEditText.text.toString()
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(context,"Registration Successful",Toast.LENGTH_SHORT).show()
                        view?.findNavController()?.navigate(R.id.action_adminInfo_to_loginScreen)
                    }
                    else Toast.makeText(context,"Registration Failed: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                }

        }
        return binding.root
    }
}