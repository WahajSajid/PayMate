package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentAdminRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentAdminRegistrationBinding
    private lateinit var supportLoaderManager: LoaderManager
    private var password:String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var email:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_registration, container, false)
        //Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.loginTextButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_adminRegistrationFragment_to_adminLoginFragment)
        }

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


        //Setting up TextWatcher for create pin EditText to check the length of pin, if the length is 4 proceed ahead otherwise throw error.
        val createPinValidator = CreatePasswordValidator(object :CreaatePasswordValidatorCallBack{
            override fun onInputValidated(isValid: Boolean) {
                if(isValid){
                    //FireBase code goes here to store user input
                  password = binding.createPasswordEditText.text.toString()
                } else{
                    binding.createPasswordEditText.error = "Invalid"
                }
            }
        })
        binding.createPasswordEditText.addTextChangedListener(createPinValidator)

        //Setting up TextWatcher for confirm pin EditText to check the length of pin, if the length is 4 proceed ahead otherwise throw error.
        val confirmPinValidator = ConfirmPasswordValidator(object:ConfirmPasswordValidatorCallBack{
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
            val name = binding.enterNameEditText.text.toString()
            registerUser(email,password,name)
        }
        return binding.root
    }
    private fun registerUser(email:String,password:String,name:String){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("admin_profiles")
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(context,"Registration Successful",Toast.LENGTH_SHORT).show()
                    myRef.child(FirebaseAuth.getInstance().currentUser?.uid!!).child("name").setValue(name)
                    view?.findNavController()?.navigate(R.id.action_adminRegistrationFragment_to_adminLoginFragment)
                } else Toast.makeText(context,"Registration Failed: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
            }
    }
}