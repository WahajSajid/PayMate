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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddMate : Fragment() {
    private lateinit var binding: FragmentAddMateBinding
    private lateinit var phoneNumber: String
    private lateinit var mateId:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_mate, container, false)

        //Setting up items for drop down view.
        val mateIds =
            arrayOf("Mate 1", "Mate 2", "Mate 3", "Mate 4", "Mate 5", "Mate 6", "Mate 7", "Mate 8")
        val adapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, mateIds)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.matesDropDown.adapter = adapter

        //Setting up onDropDownItemClick listener logic
        binding.matesDropDown.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mateId = mateIds[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }



        //Text Watcher for phone
        val phoneValidator = PhoneValidator(object : PhoneValidatorCallBck {
            override fun onInputValidate(isTrue: Boolean) {
                if (isTrue) phoneNumber = binding.enterPhoneEditText.text.toString()
                else binding.enterPhoneEditText.error = "Invalid phone"
            }
        })
        binding.enterPhoneEditText.addTextChangedListener(phoneValidator)

        binding.addMateButton.setOnClickListener {
            if (inputFieldsEmptyOrNot()) {
                if (phoneNumberValidOrNot(phoneNumber)) {
                    val name = binding.enterNameEditText.text.toString()
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
        val database = FirebaseDatabase.getInstance()
        val databaseReference =
            database.getReference("admin_profiles")
        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
            .child(mateId).child("name").setValue(name)
        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
            .child(mateId).child("phone").setValue(phone)
//        databaseReference.orderByChild("name").equalTo(name)
//            .addListenerForSingleValueEvent(object: ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if(snapshot.exists()) Toast.makeText(context,"${snapshot.exists()}",Toast.LENGTH_SHORT).show()
//                    else Toast.makeText(context,"Not found",Toast.LENGTH_SHORT).show()
//
//                }
//                override fun onCancelled(error: DatabaseError) {
//                   Toast.makeText(context,"$error",Toast.LENGTH_SHORT).show()
//                }
//            })

//        Toast.makeText(context,"Mate Added",Toast.LENGTH_SHORT).show()
//        view?.findNavController()?.navigate(R.id.action_addMate_to_adminDashboard2)
    }


}