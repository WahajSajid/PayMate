package com.application.paymate

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.application.paymate.databinding.PopupFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PopupFragment : DialogFragment() {
    private lateinit var binding: PopupFragmentBinding
    private var phoneNumber: String = ""
    private val sharedViewModel: SharedViewModel by activityViewModels()
    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater,R.layout.popup_fragment, null,false)


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

        binding.changeButton.setOnClickListener {
            val changedName = binding.changeNameEditText.text.toString()
            val changedPhone = binding.changePhoneEditText.text.toString()
                if (phoneNumberValidOrNot()) {
                    val database = FirebaseDatabase.getInstance()
                    val databaseReference = database.getReference("admin_profiles")
                    databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("Mates").child(mateIdNode).child("name").setValue(changedName)
                    databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("Mates").child(mateIdNode).child("phone").setValue(changedPhone)
                    Toast.makeText(context,"Changes Saved",Toast.LENGTH_SHORT).show()
                    dismiss()

                } else Toast.makeText(
                    context,
                    "Please input a valid phone number",
                    Toast.LENGTH_SHORT
                ).show()
        }


        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        return dialog
    }

    //Function to check if the phone number is valid or not
    private fun phoneNumberValidOrNot(): Boolean {
        var isTrueOrFalse = false
        if (binding.changePhoneEditText.text.toString().length == 11) isTrueOrFalse = true
        return isTrueOrFalse
    }


    //Function to check if the inputFields is empty or not
    private fun inputFieldEmptyOrNot(): Boolean {
        var isTrueOrFalse = false
        val inputLayout = arrayOf(binding.changeNameInputLayout, binding.changePhoneInputLayout)

        for (i in inputLayout.indices) {
            if (inputLayout[i].isEmpty()) {
                isTrueOrFalse = false
                inputLayout[i].error = "Field Cannot be empty"
            } else {
                isTrueOrFalse = true
                inputLayout[i].error = null
            }
        }

        return isTrueOrFalse
    }

}