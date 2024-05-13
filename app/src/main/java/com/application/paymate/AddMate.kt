package com.application.paymate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.application.paymate.databinding.FragmentAddMateBinding

class AddMate : Fragment() {
private lateinit var binding: FragmentAddMateBinding
private lateinit var phoneNumber:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_mate, container, false)

        //Text Watcher for phone
       val phoneValidator = PhoneValidator(object : PhoneValidatorCallBck{
           override fun onInputValidate(isTrue: Boolean) {
               if(isTrue) phoneNumber = binding.enterPhoneEditText.text.toString()
               else binding.enterPhoneEditText.error = "Invalid phone"
           }
       })
        binding.enterNameEditText.addTextChangedListener(phoneValidator)


        return binding.root
    }
}