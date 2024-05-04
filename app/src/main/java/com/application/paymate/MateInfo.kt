package com.application.paymate

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.FragmentMateInfoBinding

class MateInfo : Fragment() {
    private lateinit var binding:FragmentMateInfoBinding
    private lateinit var password:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_mate_info, container, false)

        //Setting up minimum length for phone number and pin number
        val minPinLength = 4 // Minimum Pin length
        val minCnicLength = 11 //Minimum phone number length
        val pinLengthFilter = arrayOf<InputFilter>(InputFilter.LengthFilter(minPinLength))
        val cnincLengthFilter = arrayOf<InputFilter>(InputFilter.LengthFilter(minCnicLength))
//        binding.createPasswordEditText.filters = pinLengthFilter
        binding.confirmPasswordEditText.filters = pinLengthFilter
//        binding.enterPhoneNumberEditText.filters = cnincLengthFilter

        //Setting up TextWatcher for phone number to check the length of number, if the length is 11 proceed ahead otherwise throw error.
        val emailValidator = EmailValidator(object : EmailValidatorCallBack {
            override fun onInputValidated(isValid: Boolean) {
                if (isValid) {
                    //FireBase code goes here to store user input

                } else {
                    binding.enterEmailEditText.error = "Invalid"
                }
            }
        })
        binding.enterEmailEditText.addTextChangedListener(emailValidator)

        //Setting up TextWatcher for username to check if the username is starting with admin or not and if there is any space in between.
        val usernameValidator = MateUserNameInputValidator(object : MateUserNameInputValidatorCallBack {
            override fun onInputValidated(isValid: Boolean) {
                if (isValid) {
                    //FireBase code goes here to store user input
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
//                    password = binding.createPasswordEditText.text.toString()
//                } else{
//                    binding.createPasswordEditText.error = "Invalid"
//                }
//            }
//        })
//        binding.createPasswordEditText.addTextChangedListener(createPinValidator)

        //Setting up TextWatcher for confirm pin EditText to check the length of pin, if the length is 4 proceed ahead otherwise throw error.
        val confirmPinValidator = ConfirmPasswordValidator(object:ConfirmPasswordValidatorCallBack{
            override fun onInputValidated(isValid: Boolean) {
                if(isValid){
                    if(binding.confirmPasswordEditText.text.toString() == password){
                        //FireBase code goes here to store user input
                    } else binding.confirmPasswordEditText.error = "Create and confirm pin numbers does not match"
                } else{
                    binding.confirmPasswordEditText.error = "Invalid"
                }
            }
        })
        binding.confirmPasswordEditText.addTextChangedListener(confirmPinValidator)

        return binding.root
    }
}