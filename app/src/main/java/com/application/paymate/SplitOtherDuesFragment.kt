package com.application.paymate

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.paymate.databinding.FragmentSplitOtherDuesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.sync.Mutex

class SplitOtherDuesFragment : Fragment() {
    private lateinit var binding: FragmentSplitOtherDuesBinding
    private lateinit var matesList: ArrayList<MatesInfo>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       binding = DataBindingUtil.inflate(inflater,R.layout.fragment_split_other_dues, container, false)


        matesList = ArrayList()

//        Setting up adapter for recycler View
        val recyclerView= binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = OtherDuesSplitAdapter(matesList,requireContext())
        recyclerView.adapter = adapter
        adapter.itemClickListener(object : OtherDuesSplitAdapter.OnItemClickListener{
            override fun checkBoxClickListener(checkBox:CheckBox) {
                //Implemented logic to check and uncheck the radio button on each click
                if(checkBox.isChecked) Toast.makeText(context,checkBox.text.toString(),Toast.LENGTH_SHORT).show()
            }
            override val mutex: Mutex = Mutex()
        })



        //Creating instance of Firebase Database
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("admin_profiles")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("Mates")

        binding.spinnerLayout.visibility = View.VISIBLE

        //Value Event listener to retrieve the data from firebase realtime database
        if(NetworkUtil.isNetworkAvailable(requireContext())){

            databaseReference.addValueEventListener(object :ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.spinnerLayout.visibility = View.GONE

                    //Checking if any mate is exists in the database or not. If exists then show the list is empty message
                    if(!snapshot.exists()) binding.emptyListLayout.visibility = View.VISIBLE
                    else binding.emptyListLayout.visibility = View.GONE
                    binding.listOfMatesLayout.visibility = View.VISIBLE
                    binding.spinnerLayout.visibility = View.GONE
                    for(data in snapshot.children){
                        val mateInfo = data.getValue(MatesInfo::class.java)
                        matesList.add(mateInfo!!)
                    }
                    adapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
                    binding.spinnerLayout.visibility = View.GONE
                }
            })

        } else{
            binding.spinnerLayout.visibility = View.GONE
            binding.noInternetConnectionIconLayout.visibility = View.VISIBLE
        }



        return binding.root
    }

}