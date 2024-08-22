package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentAdminRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentAdminRegistrationBinding
    private var password: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var registeringDialog: RegisteringDialog
    private lateinit var fragmentManager: FragmentManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_admin_registration,
            container,
            false
        )

        //Initializing fragment manager and fragment manager and registeringDialog
        registeringDialog = RegisteringDialog()
        fragmentManager = childFragmentManager


        //Initializing Firebase Auth
        auth = FirebaseAuth.getInstance()

        //Setting up click listener for login text button
        binding.loginTextButton.setOnClickListener {
            view?.findNavController()
                ?.navigate(R.id.action_adminRegistrationFragment_to_adminLoginFragment)
        }

        //Setting up TextWatcher for phone number to check the length of number, if the length is 11 proceed ahead otherwise throw error.
        val emailValidator = EmailValidator(object : EmailValidatorCallBack {
            override fun onInputValidated(isValid: Boolean) {
                if (isValid) {
                    email = binding.enterEmailEditText.text.toString()
                } else {
                    binding.enterEmailEditText.error = "Invalid"
                }
            }
        })
        binding.enterEmailEditText.addTextChangedListener(emailValidator)


        //Setting up TextWatcher for create pin EditText to check the length of pin, if the length is 4 proceed ahead otherwise throw error.
        val createPinValidator = CreatePasswordValidator(object : CreaatePasswordValidatorCallBack {
            override fun onInputValidated(isValid: Boolean) {
                if (isValid) {
                    password = binding.createPasswordEditText.text.toString()
                } else {
                    binding.createPasswordEditText.error = "Invalid"
                }
            }
        })
        binding.createPasswordEditText.addTextChangedListener(createPinValidator)

        //Setting up TextWatcher for confirm pin EditText to check the length of pin, if the length is 4 proceed ahead otherwise throw error.
        val confirmPinValidator =
            ConfirmPasswordValidator(object : ConfirmPasswordValidatorCallBack {
                override fun onInputValidated(isValid: Boolean) {
                    if (isValid) {
                        if (binding.confirmPasswordEditText.text.toString() == password) {
                            password = binding.confirmPasswordEditText.text.toString()
                        } else binding.confirmPasswordEditText.error =
                            "Create and confirm pin numbers does not match"
                    } else {
                        binding.confirmPasswordEditText.error = "Invalid"
                    }
                }
            })
        binding.confirmPasswordEditText.addTextChangedListener(confirmPinValidator)

        //Setting up click listener on Register Button to register the user using Firebase authentication
        binding.registerButton.setOnClickListener {
            //Checking if the checkIfInputFieldIsEmpty function returns true or false
            if (checkIfInputFieldIsEmpty()) {
                if (checkCreateAndConfirmPasswordsMatchOrNot()) {
                    registeringDialog.show(fragmentManager, "loading")
                    if (NetworkUtil.isNetworkAvailable(requireContext())) {
                        HasInternetAccess.hasInternetAccess(object : HasInternetAccessCallback {
                            override fun onInternetAvailable() {
                                val name = binding.enterNameEditText.text.toString()
                                registerUser(email, password, name)
                            }

                            override fun onInternetNotAvailable() {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        context,
                                        "Connection Timeout",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    registeringDialog.dismiss()
                                }
                            }
                        })
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                        registeringDialog.dismiss()
                    }

                } else binding.confirmPasswordInputLayout.error = "Passwords does not match"
            } else Toast.makeText(
                context,
                "Please Input all the fields before entering register",
                Toast.LENGTH_SHORT
            ).show()
        }
        return binding.root
    }

    //Function to check if the create and confirm passwords match or not
    private fun checkCreateAndConfirmPasswordsMatchOrNot(): Boolean {
        var isTrueOrFalse = false
        if (binding.createPasswordEditText.text.toString() == binding.confirmPasswordEditText.text.toString()) {
            isTrueOrFalse = true
        }
        return isTrueOrFalse
    }


    //Function to check if the input field is empty or not
    private fun checkIfInputFieldIsEmpty(): Boolean {
        var isTrueOrFalse = true
        val editTexts = arrayOf(
            binding.enterNameEditText.text.toString(),
            binding.enterEmailEditText.text.toString(),
            binding.createPasswordEditText.text.toString(),
            binding.confirmPasswordEditText.text.toString()
        )
        val editLayouts = arrayOf(
            binding.enterNameInputLayout,
            binding.enterEmailInputLayout,
            binding.createPasswordInputLayout,
            binding.confirmPasswordInputLayout
        )
        val editTextHints = arrayOf(
            binding.enterNameEditText.hint.toString(),
            binding.enterEmailEditText.hint.toString(),
            binding.createPasswordEditText.hint.toString(),
            binding.confirmPasswordEditText.hint.toString()
        )
        for (i in editTexts.indices) {
            if (editTexts[i].isEmpty()) {
                editLayouts[i].error = "${editTextHints[i]} Cannot be empty"
                isTrueOrFalse = false
            } else {
                editLayouts[i].error = null
            }
        }
        return isTrueOrFalse
    }

    //Function to register the user using Firebase authentication
    private fun registerUser(email: String, password: String, name: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("admin_profiles")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    myRef.child(FirebaseAuth.getInstance().currentUser?.uid!!).child("name")
                        .setValue(name)
                    myRef.child(FirebaseAuth.getInstance().currentUser?.uid!!).child("email")
                        .setValue(email)
                    myRef.child(FirebaseAuth.getInstance().currentUser?.uid!!).child("password")
                        .setValue(password)
                    myRef.child(FirebaseAuth.getInstance().currentUser?.uid!!).child("uid")
                        .setValue(FirebaseAuth.getInstance().currentUser?.uid!!)
                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                    registeringDialog.dismiss()
                    view?.findNavController()
                        ?.navigate(R.id.action_adminRegistrationFragment_to_adminLoginFragment)
                } else {
                    Toast.makeText(
                        context,
                        "Registration Failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    registeringDialog.dismiss()
                }
            }
    }
}