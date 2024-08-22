package com.application.paymate

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface EnableOrNotCallBack{
    fun whenEnable()
    fun whenDisable()
}


object EnableAsMateOrNot {
    fun enableOrNot(callBack: EnableOrNotCallBack){
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("As_Mate").child("enabled")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) callBack.whenEnable()
                else callBack.whenDisable()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}