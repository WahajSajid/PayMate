package com.application.paymate

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.application.paymate.databinding.FragmentAddMateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

@Suppress("NAME_SHADOWING")
class AddMate : Fragment() {
    private lateinit var binding: FragmentAddMateBinding
    private var phoneNumber: String = ""
    private var _mateIdNode: String = ""
    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_mate, container, false)

        view?.setFocusableInTouchMode(true)
        view?.requestFocus()

        val _mateId = sharedViewModel._mateId.value.toString().toInt()

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
//                    addMate(name, phoneNumber,_mateId)
                    addMateMethod(name, phoneNumber, _mateId)
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

    //Function to add the mate
    private fun addMateMethod(name: String, phone: String, _mateId: Int) {


        val trimmedName = name.trim().replace(" ", "")
        val lowerCaseName = trimmedName.lowercase(Locale.getDefault())

        //Using shared preference to store the mate id
        val sharedPreferences =
            requireContext().getSharedPreferences("mate_id", Context.MODE_PRIVATE)

        binding.spinnerLayout.visibility = View.VISIBLE
        val database = FirebaseDatabase.getInstance()


        //Getting the reference fo Mate node from the realtime database
        val mateReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")

        //Getting the reference fo all_phone_numbers node from the realtime database
        val phoneNumberReference = database.getReference("all_phone_numbers")

        //Adding the hashmap to all_phone_numbers node
        val newPhoneNumber = HashMap<String, String>()
        newPhoneNumber["phone_number"] = phone
        val databaseReference =
            database.getReference("admin_profiles")
        if (NetworkUtil.isNetworkAvailable(requireContext())) {
            databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
                .get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val getMateId = sharedPreferences.getInt("mate_id", 1)
                        val mateId = getMateId + 1
                        val id = "$lowerCaseName@$mateId"
                        sharedPreferences.edit()?.putInt("mate_id", mateId)?.apply()
                        val mateNode = "Mate: $id"
                        //Checking if the phone number already exists or not
                        mateReference.orderByChild("phone").equalTo(phoneNumber)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        Toast.makeText(
                                            context,
                                            "$phone already exists",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        binding.spinnerLayout.visibility = View.GONE
                                    } else {
                                        phoneNumberReference.push().setValue(newPhoneNumber)
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates").child(mateNode).child("mate_id")
                                            .setValue(id)
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(mateNode).child("name").setValue(name)
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(mateNode).child("phone").setValue(phone)
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(mateNode).child("rent_amount").setValue("0")
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(mateNode).child("other_amount").setValue("0")
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(mateNode).child("wallet_amount").setValue("0")
                                        Toast.makeText(context, "Mate Added", Toast.LENGTH_SHORT)
                                            .show()
                                        binding.spinnerLayout.visibility = View.GONE

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
                                        .show()
                                    binding.spinnerLayout.visibility = View.GONE
                                }
                            })

                    } else {
                        //Checking if the phone number already exists or not
                        mateReference.orderByChild("phone").equalTo(phoneNumber)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        Toast.makeText(
                                            context,
                                            "$phone already exists",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        binding.spinnerLayout.visibility = View.GONE
                                    } else {

                                        //Setting initial mate Id node
                                        val initialMateId = "$lowerCaseName@1"
                                        val initialMateNode = "Mate: $lowerCaseName@1"
                                        phoneNumberReference.push().setValue(newPhoneNumber)
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates").child(initialMateNode).child("mate_id")
                                            .setValue(initialMateId)
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(initialMateNode).child("name").setValue(name)
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(initialMateNode).child("phone").setValue(phone)
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(initialMateNode).child("rent_amount")
                                            .setValue("0")
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(initialMateNode).child("other_amount")
                                            .setValue("0")
                                        databaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid)
                                            .child("Mates")
                                            .child(initialMateNode).child("wallet_amount")
                                            .setValue("0")

                                        //Initializing the mate id with 1 and storing it using sharePreference
                                        sharedPreferences.edit()?.putInt("mate_id", 1)?.apply()
                                        Toast.makeText(context, "Mate Added", Toast.LENGTH_SHORT)
                                            .show()
                                        binding.spinnerLayout.visibility = View.GONE

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
                                        .show()
                                    binding.spinnerLayout.visibility = View.GONE
                                }
                            })
                    }
                }
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            binding.spinnerLayout.visibility = View.GONE
        }

    }

}