package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.application.paymate.databinding.FragmentAddMateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.values

@Suppress("NAME_SHADOWING")
class AddMate : Fragment() {
    private lateinit var binding: FragmentAddMateBinding
    private var phoneNumber: String = ""
    private var _mateIdNode: String = ""
    private var _mateId: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_mate, container, false)

        view?.setFocusableInTouchMode(true)
        view?.requestFocus()



        //Text Watcher for phone
        val phoneValidator = PhoneValidator(object : PhoneValidatorCallBck {
            override fun onInputValidate(isTrue: Boolean) {
                if (isTrue) {
                    val number = binding.enterPhoneEditText.text.toString()
                    phoneNumber = number
                } else binding.enterPhoneEditText.error = "Invalid phone"
            }
        })
        binding.enterPhoneEditText.addTextChangedListener(phoneValidator)

        binding.addMateButton.setOnClickListener {
            if (inputFieldsEmptyOrNot()) {
                if (phoneNumberValidOrNot(phoneNumber)) {
                    val name = binding.enterNameEditText.text.toString()
                    _mateIdNode = "Mate $_mateId"
                    addMate(name, phoneNumber)
                } else binding.enterPhoneInputLayout.error = "Invalid Phone Number"
            } else Toast.makeText(context, "Please input all the fields", Toast.LENGTH_SHORT).show()

        }


        return binding.root
    }

    //Function to check if the phoneNumber is valid or not
    private fun phoneNumberValidOrNot(phoneNumber: String): Boolean {
        val isTrueOrFalse: Boolean = binding.enterPhoneEditText.text?.length == 11
        return isTrueOrFalse
    }


    //Function to check if the input fields or empty or not.
    private fun inputFieldsEmptyOrNot(): Boolean {
        var isTrueOrFalse = false
        val inputTexts = arrayOf(
            binding.enterNameEditText.text.toString(),
            binding.enterPhoneEditText.text.toString()
        )
        val inputLayouts = arrayOf(binding.enterNameInputLayout, binding.enterPhoneInputLayout)
        val inputLayoutHints = arrayOf(
            binding.enterNameEditText.hint.toString(),
            binding.enterPhoneEditText.hint.toString()
        )
        for (i in inputTexts.indices) {
            if (inputTexts[i].isEmpty()) {
                inputLayouts[i].error = "${inputLayoutHints[i]} cannot be empty"
                isTrueOrFalse = false
            } else {
                inputLayouts[i].error = null
                isTrueOrFalse = true
            }
        }
        return isTrueOrFalse
    }


    //Function to add mate
    private fun addMate(name: String, phone: String) {
        binding.spinnerLayout.visibility = View.VISIBLE

        val database = FirebaseDatabase.getInstance()
        val databaseReference =
            database.getReference("admin_profiles")


        val mateReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
        val phoneNumberReference = database.getReference("all_phone_numbers")
        val newPhoneNumber = HashMap<String, String>()
        newPhoneNumber["phone_number"] = phone

        //Condition to check if the phone is connected to internet or not.
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            //Checking if the Mate is already present or not

            //Checking if the phone number already exists or not
            mateReference.orderByChild("phone").equalTo(phoneNumber)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            Toast.makeText(context, "$phone already exists", Toast.LENGTH_SHORT)
                                .show()
                            binding.spinnerLayout.visibility = View.GONE
                        } else {
                            phoneNumberReference.push().setValue(newPhoneNumber)
                            databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .child("Mates").child(_mateIdNode).child("mate_id")
                                .setValue(_mateId.toString())
                            databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .child("Mates")
                                .child(_mateIdNode).child("name").setValue(name)
                            databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                .child("Mates")
                                .child(_mateIdNode).child("phone").setValue(phone)
                            Toast.makeText(context, "Mate Added", Toast.LENGTH_SHORT)
                                .show()
                            databaseReference
                                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
                                .child(_mateIdNode).get()
                                .addOnSuccessListener { snapshot ->
                                    _mateId = snapshot.child("mate_id").value.toString().toInt() + 1
                                }
                            binding.spinnerLayout.visibility = View.GONE

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                        binding.spinnerLayout.visibility = View.GONE
                    }
                })
        } else {
            binding.spinnerLayout.visibility = View.GONE
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }

    }


}