package com.application.paymate

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AddAmount {


   public fun addAmount(updateChangesButton: Button,mateIdNode:String,updateContext:String, enterRentAmountEditText: EditText, context: Context,view:View){
        updateChangesButton.setOnClickListener {
            val enteredRentAmount = enterRentAmountEditText.text.toString()
            val inputFieldChecker = InputFieldEmptyOrNot()
            val database = FirebaseDatabase.getInstance()
            val databaseReference = database.getReference("admin_profiles")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
                .child(mateIdNode)
            if (inputFieldChecker.inputFieldEmptyOrNot(enterRentAmountEditText)) {
                Toast.makeText(context, "Please Enter Amount First", Toast.LENGTH_SHORT).show()
            } else {

                //Checking if the internet is available or not
                if (NetworkUtil.isNetworkAvailable(context)) {

                    //Checking if the user wants update rent
                    when (updateContext) {
                        "update_rent" -> {
                            databaseReference.child("rent_amount").get().addOnCompleteListener { snapshot ->
                                val currentRentAmount = snapshot.result.value.toString()
                                val newRentAmount = currentRentAmount.toInt() + enteredRentAmount.toInt()
                                databaseReference.child("rent_amount").setValue(newRentAmount.toString())
                                Toast.makeText(context, "Rent Updated", Toast.LENGTH_SHORT).show()
                                view.findNavController().navigate(R.id.action_rentUpdateFragment2_to_allMates2)
                            }
                        }
                        //Checking if the user wants to update other amount
                        "update_other_amount" -> {
                            databaseReference.child("other_amount").get().addOnCompleteListener { snapshot ->
                                val currentOtherAmount = snapshot.result.value.toString()
                                val newOtherAmount = currentOtherAmount.toInt() + enteredRentAmount.toInt()
                                databaseReference.child("other_amount").setValue(newOtherAmount.toString())
                                Toast.makeText(context, "Amount Updated", Toast.LENGTH_SHORT).show()
                                view.findNavController().navigate(R.id.action_otherDueUpdateFragment2_to_allMates2)
                            }
                        }
                        //Checking if the user wants to update wallet
                        "update_wallet" -> {
                            databaseReference.child("wallet_amount").get().addOnCompleteListener { snapshot ->
                                val currentOtherAmount = snapshot.result.value.toString()
                                val newOtherAmount = currentOtherAmount.toInt() + enteredRentAmount.toInt()
                                databaseReference.child("wallet_amount").setValue(newOtherAmount.toString())
                                Toast.makeText(context, "Wallet Updated", Toast.LENGTH_SHORT).show()
                                view.findNavController().navigate(R.id.action_walletUpdateFragment4_to_allMates2)
                            }
                        }
                    }


                } else Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }

}