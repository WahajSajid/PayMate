package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.application.paymate.databinding.FragmentEditDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditMateDetailsFragment : Fragment() {
    private lateinit var binding:FragmentEditDetailsBinding
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private var phoneNumber: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit_details, container, false)
        //Initializing variables from sharedView Model and edit texts
        val mateIdNode = sharedViewModel.mateNode.value.toString()
        val oldName = sharedViewModel.mateName.value.toString()
        val oldPhone = sharedViewModel.matePhone.value.toString()

        //Set the old name and phone number to edit texts initially.
        binding.changeNameEditText.setText(oldName)
        binding.changePhoneEditText.setText(oldPhone)

        //Phone Input Validator and Text Watcher
        val phoneValidator = PhoneValidator(object : PhoneValidatorCallBck {
            override fun onInputValidate(isTrue: Boolean) {
                if (isTrue) {
                    val number = binding.changeNameEditText.text.toString()
                    phoneNumber = number
                } else binding.changePhoneEditText.error = "Invalid"
            }
        })
        binding.changePhoneEditText.addTextChangedListener(phoneValidator)

        binding.saveChangesButton.setOnClickListener {
            if(NetworkUtil.isNetworkAvailable(requireContext())){


                val changedName = binding.changeNameEditText.text.toString()
                val changedPhone = binding.changePhoneEditText.text.toString()
                if (phoneNumberValidOrNot()) {
                    val database = FirebaseDatabase.getInstance()
                    val databaseReference = database.getReference("admin_profiles")
                    databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("Mates").child(mateIdNode).child("name").setValue(changedName)
                    databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("Mates").child(mateIdNode).child("phone").setValue(changedPhone)
                    Toast.makeText(context,"Changes Saved", Toast.LENGTH_SHORT).show()

                } else Toast.makeText(
                    context,
                    "Please input a valid phone number",
                    Toast.LENGTH_SHORT
                ).show()

            } else Toast.makeText(context,"No Internet Connection", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
    //Function to check if the phone number is valid or not
    private fun phoneNumberValidOrNot(): Boolean {
        var isTrueOrFalse = false
        if (binding.changePhoneEditText.text.toString().length == 11) isTrueOrFalse = true
        return isTrueOrFalse
    }
}