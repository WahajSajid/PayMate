package com.application.paymate

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.absoluteValue

class UpdateAmount(private var mateName: String) {
    @SuppressLint("SuspiciousIndentation")
    fun updateAmount(
        addToAdminAlso: String,
        amount: String,
        mateIdNode: String,
        updateContext: String,
        updateDomain: String,
        navigateBackOrNot: Boolean,
        context: Context,
        view: View
    ) {
        val database = FirebaseDatabase.getInstance()
        val databaseReference: DatabaseReference = if (addToAdminAlso == "true") {
            database.getReference("admin_profiles")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate")
        } else {
            database.getReference("admin_profiles")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")
                .child(mateIdNode)
        }

        //Checking if the user wants update rent
        when (updateContext) {
            "update_rent" -> {
                //Checking if the user wants to plus or minus the amount
                if (updateDomain == "plus") {
                    databaseReference.child("rent_amount").get()
                        .addOnCompleteListener { snapshot ->
                            val currentRentAmount = snapshot.result.value.toString()
                            val newRentAmount =
                                currentRentAmount.toInt() + amount.toInt()
                            //Calling a function to add rent and check if the wallet is available is yes then subtract the rent amount from the wallet amount
                            if (addToAdminAlso == "true") {
                                addToAdmin(
                                    databaseReference,
                                    "rent_amount",
                                    newRentAmount,
                                    context,
                                    navigateBackOrNot
                                )
                            } else {
                                addAmount(
                                    databaseReference,
                                    "rent_amount",
                                    newRentAmount,
                                    context,
                                    navigateBackOrNot
                                )
                            }
                            if (navigateBackOrNot) {
                                val navController = view.findNavController()
                                navController.popBackStack(R.id.allMates2, false)
                            }
                        }
                } else {
                    databaseReference.child("rent_amount").get()
                        .addOnCompleteListener { snapshot ->
                            val currentRentAmount = snapshot.result.value.toString()
                            val newRentAmount =
                                currentRentAmount.toInt() - amount.toInt()
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
                                if (navigateBackOrNot) {
                                    val navController = view.findNavController()
                                    navController.popBackStack(R.id.allMates2, false)
                                }
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
                                currentOtherAmount.toInt() + amount.toInt()
                            //Calling a function to add rent and check if the wallet is available is yes then subtract the rent amount from the wallet amount
                            if (addToAdminAlso == "true") {
                                addToAdmin(
                                    databaseReference,
                                    "other_amount",
                                    newOtherAmount,
                                    context,
                                    navigateBackOrNot
                                )
                            } else {
                                addAmount(
                                    databaseReference,
                                    "other_amount",
                                    newOtherAmount,
                                    context,
                                    navigateBackOrNot
                                )
                            }
                            if (navigateBackOrNot) {
                                val navController = view.findNavController()
                                navController.popBackStack(R.id.allMates2, false)
                            }
                        }
                } else {
                    databaseReference.child("other_amount").get()
                        .addOnCompleteListener { snapshot ->
                            val currentOtherAmount = snapshot.result.value.toString()
                            val newOtherAmount =
                                currentOtherAmount.toInt() - amount.toInt()
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
                                if (navigateBackOrNot) {
                                    val navController = view.findNavController()
                                    navController.popBackStack(R.id.allMates2, false)
                                }
                            }
                        }
                }
            }
            //Checking if the user wants to update wallet
            "update_wallet" -> {
                //Checking if the user wants to plus or minus the amount
                if (updateDomain == "plus") {

                    //Calling a function to update the wallet and to check if the other or rent amount is available then subtract it from the wallet amount
                    updateWallet(databaseReference, amount)
                    Toast.makeText(
                        context,
                        "Changes Saved",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    if (navigateBackOrNot) {
                        val navController = view.findNavController()
                        navController.popBackStack(R.id.allMates2, false)
                    }

                } else {
                    databaseReference.child("wallet_amount").get()
                        .addOnCompleteListener { snapshot ->
                            val currentWalletAmount = snapshot.result.value.toString()
                            val newWalletAmount =
                                currentWalletAmount.toInt() - amount.toInt()
                            if (newWalletAmount < 0) {
                                Toast.makeText(
                                    context,
                                    "Amount cannot be negative",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                databaseReference.child("wallet_amount")
                                    .setValue(newWalletAmount.toString())
                                Toast.makeText(
                                    context,
                                    "Wallet Updated",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                if (navigateBackOrNot) {
                                    val navController = view.findNavController()
                                    navController.popBackStack(R.id.allMates2, false)
                                }
                            }
                        }
                }

            }
        }
    }


    //Method to update the wallet amount and rent,other amount accordingly. Because if the wallet added it should be subtracted from the rent and other amount
    private fun updateWallet(
        databaseReference: DatabaseReference,
        enteredAmount: String,
    ) {
        var newOtherAmount: Int
        var newRentAmount: Int
        var walletAmount: Int
        databaseReference.child("wallet_amount").get()
            .addOnCompleteListener { snapshot ->
                val currentWalletAmount = snapshot.result.value.toString()
                val updatedWalletAmount =
                    currentWalletAmount.toInt() + enteredAmount.toInt()
                databaseReference.child("other_amount").get()
                    .addOnCompleteListener { other_amount ->
                        val otherAmount = other_amount.result.value.toString().toInt()
                        //Checking if thr other amount is greater than 0 or not. If yes then subtract it from the wallet amount
                        if (otherAmount > 0) {
                            walletAmount = updatedWalletAmount - otherAmount
                            if (walletAmount > 0) {
                                databaseReference.child("other_amount").setValue("0")
                                //Calling subtractRentAmountWhenWalletAdded method after subtracting wallet amount from other amount if there  is rent available and wallet amount is also remaining then subtract rent wallet amount from rent amount
                                subtractRentAmountWhenWalletAddedAfterSubtractingOtherAmount(
                                    databaseReference,
                                    walletAmount
                                )
                            } else {
                                newOtherAmount = walletAmount.absoluteValue
                                databaseReference.child("other_amount")
                                    .setValue(newOtherAmount.toString())
                                databaseReference.child("wallet_amount").setValue("0")
                            }
                            //This condition is for when the other amount is 0
                        } else {


                            databaseReference.child("rent_amount").get()
                                .addOnCompleteListener { rent_amount ->
                                    val rentAmount = rent_amount.result.value.toString().toInt()
                                    //Checking if the rent amount is greater than 0 or not. If yes then subtract it from the wallet amount
                                    if (rentAmount > 0) {
                                        walletAmount = updatedWalletAmount - rentAmount
                                        if (walletAmount > 0) {
                                            databaseReference.child("rent_amount").setValue("0")
                                            databaseReference.child("wallet_amount")
                                                .setValue(walletAmount.toString())
                                        } else {
                                            newRentAmount = walletAmount.absoluteValue
                                            databaseReference.child("rent_amount")
                                                .setValue(newRentAmount.toString())
                                            databaseReference.child("wallet_amount").setValue("0")
                                        }
                                    } else {
                                        databaseReference.child("wallet_amount")
                                            .setValue(updatedWalletAmount.toString())
                                    }
                                }
                        }
                    }
            }
    }

    //Method to add the rent amount after checking if the wallet is available or not if the wallet is available then subtract the rent amount from
    private fun addAmount(
        databaseReference: DatabaseReference,
        updateContext: String,
        amount: Int,
        context: Context,
        navigateBackOrNot: Boolean
    ) {
        var newAmount: Int
        var walletAmount: Int
        databaseReference.child("wallet_amount").get()
            .addOnCompleteListener { wallet_amount ->
                val wallet = wallet_amount.result.value.toString().toInt()
                if (wallet > 0) {
                    walletAmount = wallet - amount
                    if (walletAmount > 0) {
                        databaseReference.child(updateContext).setValue("0")
                        databaseReference.child("wallet_amount")
                            .setValue(walletAmount.toString())
                        Toast.makeText(context, "Amount Added", Toast.LENGTH_SHORT).show()

                    } else {
                        newAmount = walletAmount.absoluteValue
                        databaseReference.child(updateContext)
                            .setValue(newAmount.toString())
                        databaseReference.child("wallet_amount").setValue("0")
                        if (navigateBackOrNot) {
                            Toast.makeText(context, "Amount Added", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    databaseReference.child(updateContext).setValue(amount.toString())
                    if (navigateBackOrNot) {
                        Toast.makeText(context, "Amount Added", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


    //The method to check after subtracting wallet amount from other amount if there  is rent available and wallet amount is also remaining then subtract rent wallet amount from rent amount
    private fun subtractRentAmountWhenWalletAddedAfterSubtractingOtherAmount(
        databaseReference: DatabaseReference,
        remainingWalletAmount: Int
    ) {
        var newRentAmount: Int
        var walletAmount: Int
        databaseReference.child("rent_amount").get()
            .addOnCompleteListener { rent_amount ->
                val rentAmount = rent_amount.result.value.toString().toInt()
                //Checking if the rent amount is greater than 0 or not. If yes then subtract it from the wallet amount
                if (rentAmount > 0) {
                    walletAmount = remainingWalletAmount - rentAmount
                    if (walletAmount > 0) {
                        databaseReference.child("rent_amount").setValue("0")
                        databaseReference.child("wallet_amount")
                            .setValue(walletAmount.toString())
                    } else {
                        newRentAmount = walletAmount.absoluteValue
                        databaseReference.child("rent_amount")
                            .setValue(newRentAmount.toString())
                        databaseReference.child("wallet_amount").setValue("0")
                    }
                } else {
                    databaseReference.child("wallet_amount")
                        .setValue(remainingWalletAmount.toString())
                }
            }
    }

    private fun addToAdmin(
        databaseReference: DatabaseReference,
        updateContext: String,
        amount: Int,
        context: Context,
        navigateBackOrNot: Boolean
    ) {
        databaseReference.child(updateContext).setValue(amount)
        if (navigateBackOrNot) {
            Toast.makeText(context, "Amount Added", Toast.LENGTH_SHORT).show()
        }
    }
}