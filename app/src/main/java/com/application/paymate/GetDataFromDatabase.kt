package com.application.paymate

import android.content.SharedPreferences
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.SharingStarted

class GetDataFromDatabase(private val databaseReference: DatabaseReference,private val sharedPreferences: SharedPreferences) {

    fun getAllData(){
        getData(databaseReference.child("name"),"name")
        getData(databaseReference.child("wallet_amount"),"wallet_amount")
        getData(databaseReference.child("rent_amount"),"rent_amount")
        getData(databaseReference.child("other_amount"),"other_amount")
    }
   private fun getData(reference: DatabaseReference,localDatabaseReference: String){
       reference.addValueEventListener(object:ValueEventListener{

           override fun onDataChange(snapshot: DataSnapshot) {

           }

           override fun onCancelled(error: DatabaseError) {

           }
       })
    }

}