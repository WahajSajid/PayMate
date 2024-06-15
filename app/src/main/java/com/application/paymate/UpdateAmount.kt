package com.application.paymate

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdateAmount {


    fun updateAmount(
        updateChangesButton: Button,
        mateIdNode: String,
        updateContext: String,
        updateDomain: String,
        enterAmountEditText: EditText,
        context: Context,
        view: View
    ) {

        updateChangesButton.setOnClickListener {
            val navController = view.findNavController()
            val enteredRentAmount = enterAmountEditText.text.toString()
            val inputFieldChecker = InputFieldEmptyOrNot()
            val database = FirebaseDatabase.getInstance()
            val databaseReference = database.getReference("admin_profiles")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
                .child(mateIdNode)
            if (inputFieldChecker.inputFieldEmptyOrNot(enterAmountEditText)) {
                Toast.makeText(context, "Please Enter Amount First", Toast.LENGTH_SHORT).show()
            } else {

                //Checking if the internet is available or not
                if (NetworkUtil.isNetworkAvailable(context)) {

                    //Checking if the user wants update rent
                    when (updateContext) {
                        "update_rent" -> {
                            //Checking if the user wants to plus or minus the amount
                            if (updateDomain == "plus") {
                                databaseReference.child("rent_amount").get()
                                    .addOnCompleteListener { snapshot ->
                                        val currentRentAmount = snapshot.result.value.toString()
                                        val newRentAmount =
                                            currentRentAmount.toInt() + enteredRentAmount.toInt()
                                        databaseReference.child("rent_amount")
                                            .setValue(newRentAmount.toString())
                                        Toast.makeText(context, "Rent Updated", Toast.LENGTH_SHORT)
                                            .show()
                                        navController.popBackStack(R.id.allMates2, false)
                                    }
                            } else {
                                databaseReference.child("rent_amount").get()
                                    .addOnCompleteListener { snapshot ->
                                        val currentRentAmount = snapshot.result.value.toString()
                                        val newRentAmount =
                                            currentRentAmount.toInt() - enteredRentAmount.toInt()
                                        if (newRentAmount < 0) {
                                            Toast.makeText(
                                                context,
                                                "Amount cannot be negative",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            databaseReference.child("rent_amount")
                                                .setValue(newRentAmount.toString())
                                            Toast.makeText(
                                                context,
                                                "Rent Updated",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack(R.id.allMates2, false)
                                        }

                                    }
                            }
                        }
                        //Checking if the user wants to update other amount
                        "update_other_amount" -> {
                            //Checking if the user wants to plus or minus the amount
                            if (updateDomain == "plus") {
                                databaseReference.child("other_amount").get()
                                    .addOnCompleteListener { snapshot ->
                                        val currentOtherAmount = snapshot.result.value.toString()
                                        val newOtherAmount =
                                            currentOtherAmount.toInt() + enteredRentAmount.toInt()
                                        databaseReference.child("other_amount")
                                            .setValue(newOtherAmount.toString())
                                        Toast.makeText(
                                            context,
                                            "Amount Updated",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack(R.id.allMates2, false)
                                    }
                            } else {
                                databaseReference.child("other_amount").get()
                                    .addOnCompleteListener { snapshot ->
                                        val currentOtherAmount = snapshot.result.value.toString()
                                        val newOtherAmount =
                                            currentOtherAmount.toInt() - enteredRentAmount.toInt()
                                        if (newOtherAmount < 0) {
                                            Toast.makeText(
                                                context,
                                                "Amount cannot be negative",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            databaseReference.child("other_amount")
                                                .setValue(newOtherAmount.toString())
                                            Toast.makeText(
                                                context,
                                                "Amount Updated",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack(R.id.allMates2, false)
                                        }
                                    }
                            }
                        }
                        //Checking if the user wants to update wallet
                        "update_wallet" -> {

                            //Checking if the user wants to plus or minus the amount
                            if (updateDomain == "plus") {
                                databaseReference.child("wallet_amount").get()
                                    .addOnCompleteListener { snapshot ->
                                        val currentWalletAmount = snapshot.result.value.toString()
                                        val newWalletAmount =
                                            currentWalletAmount.toInt() + enteredRentAmount.toInt()
                                        databaseReference.child("wallet_amount")
                                            .setValue(newWalletAmount.toString())
                                        Toast.makeText(
                                            context,
                                            "Wallet Updated",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        navController.popBackStack(R.id.allMates2, false)
                                    }
                            } else {
                                databaseReference.child("wallet_amount").get()
                                    .addOnCompleteListener { snapshot ->
                                        val currentWalletAmount = snapshot.result.value.toString()
                                        val newWalletAmount =
                                            currentWalletAmount.toInt() - enteredRentAmount.toInt()
                                        if(newWalletAmount < 0){
                                            Toast.makeText(context, "Amount cannot be negative", Toast.LENGTH_SHORT).show()
                                        } else{
                                            databaseReference.child("wallet_amount")
                                                .setValue(newWalletAmount.toString())
                                            Toast.makeText(context, "Wallet Updated", Toast.LENGTH_SHORT)
                                                .show()
                                            navController.popBackStack(R.id.allMates2, false)
                                        }
                                    }
                            }

                        }
                    }


                } else Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }
}